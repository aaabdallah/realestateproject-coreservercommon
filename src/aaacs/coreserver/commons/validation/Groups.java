package aaacs.coreserver.commons.validation;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Ahmed A. Abd-Allah
 * Created on Oct 29, 2006
 *
 * A convenience class to hide the implementation details of groups. Currently the implementation
 * uses a simple string of all the groups concatenated with each other. Not pretty but it works.
 */
@Embeddable
public class Groups implements Serializable
{
	// ----- Static members ---------------------------------------------------
	/**
	 * Serialization Version Number
	 */
	private static final long serialVersionUID = 1000L;

	public static final String UNINITIALIZED = "__UNINITIALIZED__";
	public static final String INVALID = "__INVALID__";
	public static final String MATCH_ALL_GROUPS = "__ALL__";

	// every group can be up to 20 characters long
	// NOTE: CHANGING THE VALUE HERE IS NOT ENOUGH, YOU MUST ALSO CHANGE VALUES IN "isValidGroups"
	// NOTE2: CHANGING THE VALUE HERE MEANS CHANGING THE TABLE tGroups.name COLUMN WIDTH!
	public static final int maxGroupLength = 20; 
	// the maximum space available is 220 characters or up to 10 groups (plus brackets around each)
	public static final int maxGroupsLength = 220;

	public static boolean isValidGroups(String inGroups)
	{
		if (inGroups == null || inGroups.equals(Groups.INVALID))
			return false;

		if (inGroups.equals(Groups.UNINITIALIZED) || inGroups.equals("") || inGroups.equals(Groups.MATCH_ALL_GROUPS) ||
			// A string consisting of one or more tokens. Each token can be one of three patterns
			// 1. A string that is a single character enveloped by '[' and ']'. That character CANNOT be
			//    either '[' or ']' naturally.
			// 2. A string that starts with '[', followed up by any character BUT '_', '[', or ']', 
			//    followed by 1 to 19 characters that are NOT '[' or ']', followed by ']'.
			// 3. A string that starts with '[', followed by a '_', followed by any character BUT
			//    '_', '[', or ']', followed by 1 to 18 characters that are NOT '[' or ']', followed by ']'.
			inGroups.matches("(\\[[^\\[\\]]\\]|\\[[^_\\[\\]][^\\[\\]]{1,19}\\]|\\[_[^_\\[\\]][^\\[\\]]{1,18}\\])+"))
			return true;
		return false;
	}

	// We could put this method inside "GroupBean" but it's here to centralize all the validation
	// code for a single group and a set of groups (the latter being the real focus of this class).
	public static boolean isValidGroup(String group)
	{
		if ((group == null) ||
			(group.length() == 0) ||
			(group.length() > maxGroupLength) ||
			(group.startsWith("__")) ||
			(group.indexOf('[') >= 0) ||
			(group.indexOf(']') >= 0))
			return false;
		return true;		
	}

	// ----- Instance members -------------------------------------------------
	@Basic @Column(name="\"groups\"") private String groups = null;

	public Groups(Groups inGroups)
	{
		setGroups(inGroups.groups);
	}

	public Groups(String inGroups)
	{
		setGroups(inGroups);
	}
	
	public Groups()
	{
		this(Groups.UNINITIALIZED); 
	}

	public boolean isUninitialized()
	{
		return groups.equals(Groups.UNINITIALIZED);
	}

	public boolean isEmpty()
	{
		return groups.equals("");
	}

	public boolean isMatchingAll()
	{
		return groups.equals(Groups.MATCH_ALL_GROUPS);
	}

	public boolean addGroup(String group)
	{
		if (!Groups.isValidGroup(group) ||
			((groups.length() + group.length()) > maxGroupsLength))
			return false;

		if (isUninitialized() || isEmpty() || isMatchingAll())
			groups = "[" + group + "]";
		else
			groups = groups + "[" + group + "]";

		return true;
	}
	
	public boolean deleteGroup(String group)
	{
		if (!Groups.isValidGroup(group) ||
			((groups.length() + group.length()) > maxGroupsLength))
			return false;

		groups = groups.replaceAll("\\[" + group + "\\]", "");
		return true;
	}

	public boolean inGroups(String group)
	{
		if (!Groups.isValidGroup(group))
			return false;

		if (groups.indexOf("[" + group + "]") >= 0)
			return true;

		return false;
	}
	
	public boolean isValid()
	{
		return Groups.isValidGroups(groups);
	}

	public void setGroups(String inGroups)
	{
		// check that composed of [whatever]* where whatever doesn't start with __ or contain [,]
		if (Groups.isValidGroups(inGroups))
			groups = inGroups;
		else
			groups = Groups.INVALID;
	}

	public String toString()
	{
		return groups;
	}
}
