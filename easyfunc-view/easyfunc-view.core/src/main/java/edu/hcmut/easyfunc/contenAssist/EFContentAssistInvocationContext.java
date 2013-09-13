package edu.hcmut.easyfunc.contenAssist;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class EFContentAssistInvocationContext extends
		JavaContentAssistInvocationContext {
	public static final char EF_TRIGGER = '\'';

	private CharSequence fPrefix;
	private boolean ignoreSpace = false;

	public EFContentAssistInvocationContext(
			ContentAssistInvocationContext context) {
		// TODO Auto-generated constructor stub
		super(context.getViewer(), context.getInvocationOffset(), JavaPlugin
				.getActivePage().getActiveEditor());
	}

	@Override
	public CharSequence computeIdentifierPrefix() throws BadLocationException {
		// TODO Auto-generated method stub
		if (fPrefix == null) {
			IDocument document = getDocument();
			if (document == null)
				return null;
			int end = getInvocationOffset();
			int start = end;
			if (document.getChar(--start) == EF_TRIGGER)
				ignoreSpace = true;
			while (--start >= 0) {
				if (!Character.isJavaIdentifierPart(document.getChar(start))) {

					if (ignoreSpace) {
						if (Character.isWhitespace(document.getChar(start))
								|| document.getChar(start) == '<'
								|| document.getChar(start) == '>') {
							continue;
						}
						end--;
					}
					break;
				}

			}
			start++;
			fPrefix = document.get(start, end - start);
		}

		return fPrefix;
	}

	public boolean getIgnoreSpace() {
		return ignoreSpace;
	}
}
