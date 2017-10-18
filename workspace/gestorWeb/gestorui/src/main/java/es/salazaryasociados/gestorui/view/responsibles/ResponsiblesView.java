package es.salazaryasociados.gestorui.view.responsibles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.omg.CORBA.UserException;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import es.salazaryasociados.gestorui.SalazarBean;
import es.salazaryasociados.services.data.api.IDataService;
import es.salazaryasociados.services.data.dto.EventDTO;
import es.salazaryasociados.services.data.dto.PaymentDTO;
import es.salazaryasociados.services.data.dto.ResponsibleDTO;
import es.salazaryasociados.services.data.dto.ResponsibleFilesDTO;
import es.salazaryasociados.services.data.exceptions.DataServiceException;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean
@ViewScoped
public class ResponsiblesView extends SalazarBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3179959503785543894L;

	private static final String ACTION_BUTTON = "actionButton";
	private static final String DELETE = "delete";
	private static final String UPDATE = "update";
	private static final String SAVE = "save";
	private static final String INIT_CREATE = "initCreate";

	private static final String REPORT_PATH = "./reports/filesPerResp.jrxml";

	@Inject
	protected IDataService dataService;
	@Getter
	@Setter
	private List<ResponsibleDTO> responsibles;
	@Getter
	@Setter
	private ResponsibleDTO selectedResponsible;
	private ResponsibleFilesDataModel filesDataModel;

	private String actionButton = null;

	@Getter
	@Setter
	private String graph = "PagosXResponsable";
	@Getter
	@Setter
	private List<SelectItem> graphs;
	@Getter
	@Setter
	private BarChartModel barModel = new BarChartModel();

	@PostConstruct
	public void init() {
		super.init();

		initData();
	}

	private void initData() {
		try {
			responsibles = dataService.getAllResponsibles(-1, 0, null, null, false);
			filesDataModel = new ResponsibleFilesDataModel(dataService);

			graphs = new ArrayList<SelectItem>();
			graphs.add(new SelectItem("PagosXResponsable", "Pagos x Responsable"));
			graphs.add(new SelectItem("PagosXAnno", "Pagos x Año"));
			graphs.add(new SelectItem("PagosXAsunto", "Pagos x Asunto"));

		} catch (DataServiceException e) {
			responsibles = new ArrayList<ResponsibleDTO>();
		}
	}

	public ResponsibleFilesDataModel getFilesDataModel() {
		filesDataModel.setResponsible(selectedResponsible);
		return filesDataModel;
	}

	/**
	 * Recoge el evento de la vista
	 * 
	 * @param event
	 * @throws UserException
	 */
	public void attrListener(ActionEvent event) {
		actionButton = (String) event.getComponent().getAttributes().get(ACTION_BUTTON);
	}

	/**
	 * Controla la acción de los botones de la vista. CREA, EDITA, BORRA
	 * 
	 * @return Pantalla a la que se dirige
	 * @throws UserException
	 */
	public String editAction() throws UserException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (actionButton != null) {
			try {
				switch (actionButton) {
				case INIT_CREATE:
					initData();
					selectedResponsible = new ResponsibleDTO();
					break;
				case UPDATE:
					break;
				case SAVE:
					save();
					break;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
				context.validationFailed();
			}
		}

		return null; // Devuelve a la misma pantalla
	}

	private void save() {
		try {
			dataService.saveResponsible(selectedResponsible);			
		} catch (DataServiceException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}
		finally {
			initData();
		}
	}

	public void deleteResponsible() {
		try {
			dataService.deleteResponsible(selectedResponsible);
			responsibles.remove(selectedResponsible);
			selectedResponsible = null;
		} catch (DataServiceException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.getMessage()));
			context.validationFailed();
		}		
	}
	
	public void buildChart() {
		createModel();
	}

	private void createModel() {
		barModel = new BarChartModel();
		barModel.setAnimate(true);

		if (graph.equals("PagosXResponsable")) {
			ChartSeries pagosSerie = new ChartSeries();
			pagosSerie.setLabel("Pagos");

			for (ResponsibleDTO r : responsibles) {
				double pagos = calculatePagos(r);
				pagosSerie.set(r.getNombre(), pagos);
			}

			barModel.addSeries(pagosSerie);
		} else if (graph.equals("PagosXAnno")) {
			ChartSeries pagosSerie = new ChartSeries();
			pagosSerie.setLabel("Pagos");

			HashMap<Integer, Double> pagosMap = new HashMap<Integer, Double>();
			try {
				List<PaymentDTO> pagos = dataService.getAllPayments(-1, 0, null, "fecha", true);
				Calendar cal = Calendar.getInstance();
				for (PaymentDTO pago : pagos) {
					cal.setTimeInMillis(pago.getFecha().getTime());
					Integer y = cal.get(Calendar.YEAR);
					if (pagosMap.containsKey(y)) {
						pagosMap.put(y, pagosMap.get(y).doubleValue() + pago.getCantidad().doubleValue());
					} else {
						pagosMap.put(y, pago.getCantidad().doubleValue());
					}
				}

				for (Integer y : pagosMap.keySet()) {
					pagosSerie.set(y.toString(), pagosMap.get(y));
				}

				barModel.addSeries(pagosSerie);
			} catch (DataServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (graph.equals("PagosXAsunto")) {
			ChartSeries pagosSerie = new ChartSeries();
			pagosSerie.setLabel("Pagos");

			HashMap<String, Double> pagosMap = new HashMap<String, Double>();
			try {
				List<PaymentDTO> pagos = dataService.getAllPayments(-1, 0, null, null, true);
				for (PaymentDTO pago : pagos) {

					String asunto = pago.getFile().getAsunto();
					if (pagosMap.containsKey(asunto)) {
						pagosMap.put(asunto, pagosMap.get(asunto).doubleValue() + pago.getCantidad().doubleValue());
					} else {
						pagosMap.put(asunto, pago.getCantidad().doubleValue());
					}
				}

				ArrayList<String> max = new ArrayList<String>();
				double others = 0.0;
				int SIZE = 20;
				for (String asunto : pagosMap.keySet()) {

					int i = 0;
					for (i = 0; i < SIZE; i++) {
						if (max.size() > i) {
							double value = pagosMap.get(max.get(i));
							if (value < pagosMap.get(asunto)) {
								others += pagosMap.get(max.get(i));
								max.add(i, asunto);
								break;
							}
						} else {
							max.add(asunto);
							break;
						}
					}
					if (i == SIZE) {
						others += pagosMap.get(asunto);
					}
					if (max.size() > SIZE) {
						others += pagosMap.get(max.get(SIZE));
						max.remove(SIZE);
					}
				}

				for (String asunto : max) {
					pagosSerie.set(asunto.substring(0, 6), pagosMap.get(asunto));
				}
				// pagosSerie.set("Otros", others);

				barModel.addSeries(pagosSerie);
			} catch (DataServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private double calculatePagos(ResponsibleDTO r) {
		double result = 0.0;
		try {
			Map<String, Object> filtersAux = new HashMap<String, Object>();
			filtersAux.put("responsable1", r);
			List<ResponsibleFilesDTO> exps = dataService.getAllResponsibleFiles(-1, 0, filtersAux, null, false);
			for (ResponsibleFilesDTO e : exps) {
				result += e.getPagado().doubleValue();
			}
		} catch (DataServiceException e) {
			result = 0.0;
			e.printStackTrace();
		}
		return result;
	}

	public TimelineModel getTimelineModel() {

		TimelineModel result = new TimelineModel();
		if (selectedResponsible != null && selectedResponsible.getId() != null) {

			try {
				List<EventDTO> events = dataService.getAllEventsFromResponsible(selectedResponsible.getId());
				for (EventDTO event : events) {
					String data = "Expediente: " + event.getFile().getId() + ". " + event.getTitulo();
					TimelineEvent tevent = new TimelineEvent(data, event.getFechaInicio());
					result.add(tevent);
				}
			} catch (DataServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;
	}

	public void onTimelineEventSelect(TimelineSelectEvent e) {
		TimelineEvent timelineEvent = e.getTimelineEvent();

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, timelineEvent.getData().toString(),
				timelineEvent.getStartDate().toString());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void preparePrint () {
		
	}
	
	public StreamedContent print() {
	        
		// Preparing parameters
		Map parameters = new HashMap();
		parameters.put("responsable", selectedResponsible.getNombre());
		parameters.put("DataFile", "CustomDataSource.java");

		try {

			// Recuperamos el fichero fuente
			JasperDesign jd = JRXmlLoader.load(REPORT_PATH);
			// Compilamos el informe jrxml
			JasperReport report = JasperCompileManager.compileReport(jd);
			// Rellenamos el informe con la conexion creada y sus parametros
			// establecidos
			JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRResponsibleFilesDS(dataService,
					selectedResponsible, filesDataModel.getIncludeResponsible2(), filesDataModel.getIncludeClosed()));

			String REPORT_EXPORT_PATH = "./data/tmp/" + (new Date()).getTime() + ".pdf";
			
			JasperExportManager.exportReportToPdfFile(print, REPORT_EXPORT_PATH);
			
			File f = new File(REPORT_EXPORT_PATH);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			FileInputStream stream = new FileInputStream(f);
	        StreamedContent downloadFile = new DefaultStreamedContent(stream, externalContext.getMimeType(f.getName()), f.getName());
	        
	        return downloadFile;			
	        
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
