cchlib-servlet

---------------------------------------------------------------------
4.2.0
  Rename module from cchlib-j2ee to cchlib-servlet
---------------------------------------------------------------------
4.1.7
  Fix some javadoc issues
  Java 1.6 required (Java 1.7 no more needed)
---------------------------------------------------------------------
4.1.6
  Add support of some Java 1.7 features.
  Generated target code now is Java7
  Use some new class from core
---------------------------------------------------------------------
4.1.5
  New module: cchlib-j2ee-deprecated (move all deprecated classes from
  version 3 to cchlib-j2ee-deprecated module)
  Fix (according to evolutions of core): cx.ath.choisnet.servlet.debug.impl.InfosServletDisplayImpl2
---------------------------------------------------------------------
4.1.2
  Moving 2 maven (remove ant build number)
  Prepare to javaee6
  Add: cx.ath.choisnet.servlet.ChainingHttpServletResponse
  Add: cx.ath.choisnet.servlet.ChainingHttpServletResponseWrapper
  Add: cx.ath.choisnet.servlet.debug.DebugChainingHttpServletResponseWrapper
  Add: cx.ath.choisnet.servlet.debug.DebugHttpServletResponseWrapper
  Add: cx.ath.choisnet.servlet.debug.DebugServletResponseWrapper
---------------------------------------------------------------------
04.00 Maven version
---------------------------------------------------------------------
03a.10
Rename: com.googlecode.chclib.servlet.action.SQLServletAction to
  com.googlecode.chclib.servlet.action.AbstractSQLServletAction
---------------------------------------------------------------------
03a.09
Add:
  com.googlecode.chclib.servlet.action.AbstractServletAction
  com.googlecode.chclib.servlet.action.AbstractSimpleQueryServletAction
  com.googlecode.chclib.servlet.action.NopServletAction
  com.googlecode.chclib.servlet.action.ServletAction
  com.googlecode.chclib.servlet.action.SQLServletAction
  com.googlecode.chclib.servlet.ActionServlet
  com.googlecode.chclib.servlet.exception.RequestParameterNotFoundException
  com.googlecode.chclib.servlet.exception.RequestParameterNumberFormatException
  com.googlecode.chclib.servlet.exception.ServletActionAssertException
  com.googlecode.chclib.servlet.exception.ServletActionException
  com.googlecode.chclib.servlet.Tools
---------------------------------------------------------------------
03a.08
  Add Method cx.ath.choisnet.servlet.debug.InfosServlet.appendHTML( Appendable, HttpServlet, HttpServletRequest, HttpServletResponse );
  Add Method cx.ath.choisnet.servlet.debug.InfosServlet.appendHTML( Appendable, HttpServlet, PageContext );
---------------------------------------------------------------------
03a.07
  Fix: cx.ath.choisnet.servlet.debug.InfosServlet
---------------------------------------------------------------------
03a.07
  Add to repository (alpha not complete)
---------------------------------------------------------------------

---------------------------------------------------------------------
Known problems:
---------------------------------------------------------------------
To add/improve:
---------------------------------------------------------------------
Future extensions:
---------------------------------------------------------------------
