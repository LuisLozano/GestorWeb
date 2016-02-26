package es.salazaryasociados.gestorui.extension.view.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

import es.llaroqui.services.ServicesProvider;
import es.salazaryasociados.db.exceptions.DataException;
import es.salazaryasociados.db.model.Role;
import es.salazaryasociados.db.model.User;
import es.salazaryasociados.db.service.IDataService;

@ManagedBean
@RequestScoped
public class AdminBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3068880663461683642L;
	
	private String username;
	private String password;
	private String password2;
	private List<Role> allRoles;
	private List<User> allUsers;
	private User selectedUser;
	private DualListModel<String> roles = new DualListModel<String>();
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<User> getAllUsers() {
		return allUsers;
	}
	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
	public User getSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}	
	public DualListModel<String> getRoles() {
		return roles;
	}
	public void setRoles(DualListModel<String> roles) {
		this.roles = roles;
	}
	
	@PostConstruct
	public void init(){
		try {
			IDataService service = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
			allRoles = service.getAllRoles(-1, 0, null, null, false);			
			allUsers = new ArrayList<User>(service.getAllUsers(-1, 0, null, null, false));
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createUser()
	{
		try {			
			User user = selectedUser;
			
			if (user == null)
			{
				user = new User();
			}
			
			user.setUsername(username);
			user.setPassword(password);

			user.getRoles().clear();
			
			for (Role r : allRoles){
				for (String tName : roles.getTarget()){
					if (tName.equals(r.getRole())){
						user.getRoles().add(r);
						break;
					}
				}
			}
			
			IDataService service = ServicesProvider.GetInstance().getService(es.salazaryasociados.db.service.IDataService.class);
			service.saveUser(user);
			
			if (user != selectedUser)
			{
				allUsers.add(user);
				selectedUser = user;
			}
			
		} catch (DataException e) {
			FacesContext context = FacesContext.getCurrentInstance();	         
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  e.getMessage()) );			
		}
		catch(Exception e2){
			e2.printStackTrace();
		}
	}
	
	 public void onUserSelect(SelectEvent event) {
		 selectUser((User)event.getObject());
	 }
	 
	public void onUserUnselect(SelectEvent event) {
		selectUser(null);
	 }
	
	private void selectUser(User user){
		
		selectedUser = user;
		if (user != null){			 
			 username = selectedUser.getUsername();
			 password = selectedUser.getPassword();
			 
			 roles.setTarget(seletedUserRoles());
			 roles.setSource(selectedUserNoRoles());
		}
		else
		{
			 username = "";
			 password = "";
			 password2 = "";
			 roles.setSource(new ArrayList<String>());
			 roles.setTarget(new ArrayList<String>());			
		}
	}
	 
	 public void newUser(){
		 username = "nuevo";
		 password = "";
		 password2 = "";
		 selectedUser = null;
		 roles.setSource(selectedUserNoRoles());
		 roles.setTarget(new ArrayList<String>());
	 }
	 
	 private List<String> selectedUserNoRoles() {
		 List<String> result = new ArrayList<String>();
		 
		 for (Role r : allRoles){
			 boolean found = false;
			 if (selectedUser != null)
			 {
				 for (Role r2 : selectedUser.getRoles())
				 {
					 if (r2.getRole().equals(r.getRole()))
					 {
						 found = true;
					 }
				 }
			 }
			 if (!found){
				 result.add(r.getRole());
			 }
		 }
		return result;
	}
	private List<String> seletedUserRoles() {
		 List<String> result = new ArrayList<String>();
		 
		 for (Role r : allRoles){
			 boolean found = false;
			 if (selectedUser != null)
			 {
				 for (Role r2 : selectedUser.getRoles())
				 {
					 if (r2.getRole().equals(r.getRole()))
					 {
						 found = true;
					 }
				 }
			 }
			 if (found){
				 result.add(r.getRole());
			 }
		 }
		return result;
	}
	 
}
