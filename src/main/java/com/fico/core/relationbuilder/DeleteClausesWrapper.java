package com.fico.core.relationbuilder;

import java.util.List;

/**
 * @author AnubhavDas
 *
 * @implSpec
 * Wraps the ordered {@link List} of DELETE clauses in order to support
 * request binding from UI, as direct collection types cannot be mapped via
 * request parameter binding
 */
public class DeleteClausesWrapper {
	
	private List<String> deleteClauses;

	public List<String> getDeleteClauses() {
		return deleteClauses;
	}

	public void setDeleteClauses(List<String> deleteClauses) {
		this.deleteClauses = deleteClauses;
	}
	
}
