package aaacs.coreserver.commons.generation;

public class CodeFragmentReference
{
	public String reference = null;
	public Integer baseIndent = null;
	public Boolean keepExtraIndents = null;
	public String startComment = null;
	public String endComment = null;
	
	public CodeFragmentReference(String reference, String baseIndent, 
		String keepExtraIndents, String startComment, String endComment)
	{
		this.reference = reference;
		if (baseIndent != null)
			this.baseIndent = Integer.parseInt(baseIndent);
		if (keepExtraIndents != null)
			this.keepExtraIndents = Boolean.parseBoolean(keepExtraIndents);
		this.startComment = startComment;
		this.endComment = endComment;
	}
}
