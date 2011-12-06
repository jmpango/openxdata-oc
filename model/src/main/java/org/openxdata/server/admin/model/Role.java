package org.openxdata.server.admin.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.bind.CycleRecoverable;

/**
 * This class model a set of related permission that could be assigned to user
 * to carry out a particular task. An example could be a Data Entry role which
 * could have permissions like View Form Data, Edit Form Data, Delete Form Data,
 * and possibly more.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "role")
public class Role extends AbstractEditable implements CycleRecoverable {

	private static final long serialVersionUID = 5331693197915876841L;

	/** The name of the role. */
	@XmlElement
	private String name;

	/**
	 * The description of the role. This just throws some more light about what
	 * the role does.
	 */
	@XmlElement
	private String description;

	/** The set of permissions that the role has. */
	@XmlElementWrapper(name = "permissions", required = false)
	@XmlElement(name = "permission", type = Permission.class)
	private List<Permission> permissions;

	@XmlElementWrapper(name = "users", required = false)
	@XmlElement(name = "user", type = User.class)
	private List<User> users;

	/**
	 * Constructs a new role object.
	 */
	public Role() {
		permissions = new ArrayList<Permission>();
	}

	/**
	 * Constructs a new role object with a given name.
	 * 
	 * @param name
	 *            the name of the role.
	 */
	public Role(String name) {
		this();
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public void addPermission(Permission permission) {
		this.permissions.add(permission);
	}

	/**
	 * Returns the users associated to this Role
	 * 
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * Set the Users attached to this Role.
	 * 
	 * @param users
	 *            the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void addPermissions(List<Permission> permissions) {
		for (Permission x : permissions) {
			if (!this.permissions.contains(x))
				this.permissions.add(x);
		}
	}

	public void removePermission(Permission permission) {
		for (Permission x : permissions) {
			if (x.getName().equals(permission.getName())) {
				permissions.remove(x);
				break;
			}
		}
	}

	/**
	 * Checks if the given <code>Role</code> is the default administrator <code>Role</code> that ships with the system.
	 * @param role <code>Role</code> to check.
	 * @return <code>True only and only if role.getName().equals("Role_Administrator")
	 */
	public boolean isDefaultAdminRole() {
		return this.getName().equals("Role_Administrator");
	}
	
	/** 
	 * Used by JAXB when an object cyclic redundancy is detected in this class.
	 */
	public Role onCycleDetected(Context context) {
		Role replacement = new Role();
		replacement.setId(this.getId());
		return replacement;
	}
}
