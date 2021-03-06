cchlib-core

---------------------------------------------------------------------
4.1.7
  Fix some javadoc issues
  Move some classes from alpha.cx.ath.choisnet to alpha.com.googlecode.cchlib
  Add: com.googlecode.cchlib.swing.batchrunner.BatchRunnerInterruptedException
  Add: com.googlecode.cchlib.swing.batchrunner.BatchRunnerPanel
  Add: com.googlecode.cchlib.swing.batchrunner.BatchRunnerPanelLocaleResources
  Add: com.googlecode.cchlib.swing.batchrunner.BatchRunnerPanelWB
  Add: com.googlecode.cchlib.swing.batchrunner.DefaultBatchRunnerJFrame
  Add: com.googlecode.cchlib.swing.batchrunner.LazyBatchRunner
  Add: com.googlecode.cchlib.swing.batchrunner.LazyBatchRunnerApp
  Add: com.googlecode.cchlib.swing.batchrunner.LazyBatchRunnerLocaleResources
  Add: com.googlecode.cchlib.swing.filechooser.WaitingDialog
  Add: com.googlecode.cchlib.swing.filechooser.WaitingJDialogWB
  Add: com.googlecode.cchlib.swing.filechooser.WaitingJFileChooserInitializer

  Fix: com.googlecode.cchlib.swing.CustomDialogWB
  Fix: com.googlecode.cchlib.swing.DialogHelper

  Java 1.6 required
  Code that need Java 1.7 has been moved to cchlib-swing
  Add some javadoc

---------------------------------------------------------------------
4.1.6
  Add support of some Java 1.7 features.
  Generated code now is Java7
  Add: cx.ath.choisnet.swing.filechooser.accessory.ResourcesUtils
  Add: cx.ath.choisnet.swing.filechooser.accessory.ResourcesUtilsException
  Add: cx.ath.choisnet.swing.filechooser.JFileChooserInitializerEvent
  Add: cx.ath.choisnet.swing.filechooser.JFileChooserInitializerException
  Add: cx.ath.choisnet.swing.filechooser.JFileChooserInitializerListener
  Add: cx.ath.choisnet.swing.filechooser.accessory.ResourcesUtilsTest
  Add: cx.ath.choisnet.lang.reflect.DefaultMappableBuilderFactory
  Add: cx.ath.choisnet.swing.filechooser.WaitingDialog
  Add: cx.ath.choisnet.swing.filechooser.WaitingJDialogWB
  Add: cx.ath.choisnet.swing.filechooser.WaitingJFileChooserInitializer
  Add: cx.ath.choisnet.swing.list.LeftDotListCellRenderer
  Add: cx.ath.choisnet.swing.table.LeftDotTableCellRenderer
  Add: cx.ath.choisnet.lang.reflect.MappableBuilderTest
  Add: cx.ath.choisnet.lang.reflect.MappableHelperTest

  Add: cx.ath.choisnet.i18n.I18nSimpleStatsResourceBundle
  Fix: cx.ath.choisnet.swing.filechooser.JFileChooserInitializer (when changing
       lookandfeel while JFileChooser not yet initialised)

  Fix: cx.ath.choisnet.swing.filechooser.JFileChooserInitializer (when changing
       lookandfeel while JFileChooser initialised)
---------------------------------------------------------------------
4.1.5
  Add many documentation
  Improve many test cases
  Add: cx.ath.choisnet.util.ArrayHelper.createArray( T...entries)
  Fix: Some fix for cx.ath.choisnet.util.base64 package (more work todo)
  Add: cx.ath.choisnet.io.InputStreamHelper.toByteArray(InputStream)
  Add: cx.ath.choisnet.io.InputStreamHelper.isEquals(InputStream,InputStream)
  Add: cx.ath.choisnet.io.InputStreamHelper.isEquals(InputStream,byte[])
  Add: cx.ath.choisnet.test.Assert.assertEquals(String,char[],char[])
  Add: cx.ath.choisnet.test.SerializableTestCaseHelper
  Fix: some problems with resources (since maven migration)
  Add: com.googlecode.cchlib.io.filetype.FileDataTypes
  Add: com.googlecode.cchlib.io.filetype.package-info
  Add: com.googlecode.cchlib.net.download.URLCache
  Add: com.googlecode.cchlib.net.download.package-info
  Add: cx.ath.choisnet.lang.reflect.MappableBuilder
  Add: cx.ath.choisnet.lang.reflect.MappableBuilderDefaultFactory
  Add: cx.ath.choisnet.lang.reflect.MappableBuilderFactory
  Add: cx.ath.choisnet.lang.reflect.MappableItem
  Rem: cx.ath.choisnet.lang.reflect.MappableHelperDefaultFactory
  Rem: cx.ath.choisnet.lang.reflect.MappableHelperFactory
  Fix (according to evolutions): cx.ath.choisnet.lang.reflect.AbstractMappable
  Fix (according to evolutions): cx.ath.choisnet.lang.reflect.MappableHelper
  Add-test: cx.ath.choisnet.lang.reflect.MappableTest
  DatabaseMetaDataCollector use MappableBuilder for results
  Fix: cx.ath.choisnet.sql.export.ExportSQL correct an exportAll() issue
  Fix: cx.ath.choisnet.sql.export.ExportSQL where "whereClause" is not null
  Add: cx.ath.choisnet.swing.menu.JPopupMenuForJTextField
  Add: cx.ath.choisnet.swing.XTextField
  Modif: cx.ath.choisnet.swing.list.JPopupMenuForJList
  Modif: cx.ath.choisnet.swing.menu.AbstractJPopupMenuBuilder
  Modif: cx.ath.choisnet.swing.table.JPopupMenuForJTable
---------------------------------------------------------------------
4.1.3
  Add: cx.ath.choisnet.sql.ConnectionQuery
  Update: cx.ath.choisnet.sql.SimpleDataSource
  Update: cx.ath.choisnet.sql.SimpleQuery
  Update: cx.ath.choisnet.sql.SimpleUpdate
  Add: cx.ath.choisnet.sql.SQLCloseException
  Update: cx.ath.choisnet.lang.ByteArrayBuilder
    ByteArrayBuilder.append(byte[] bytes, int offset, int len) become final
    add: ByteArrayBuilder.hashCode()
    add: ByteArrayBuilder.replaceAll(byte[],byte[])
    improve test cases, doc.
  Fix: JDBC prior 4 cx.ath.choisnet.sql.ConnectionQuery
  Fix: cx.ath.choisnet.sql.SQLTools.parseFieldValue(String)
       cx.ath.choisnet.sql.SQLTools.parseFieldValue(String,int)
       Problems with token at the begin or at the end of String.
  Deprecated: cx.ath.choisnet.test.TstCaseHelper
  Add: cx.ath.choisnet.test.Assert
  Add: cx.ath.choisnet.test.AssertHelper
  Add: cx.ath.choisnet.util.StringHelper
  Add: cx.ath.choisnet.util.StringHelperTest
  Improve some test case
  Add: cx.ath.choisnet.sql.ExtendedSQLException
---------------------------------------------------------------------
4.1.2
  Moving 2 maven (remove ant build number)
  Doc: cx.ath.choisnet.io.FileHelper
  Doc: cx.ath.choisnet.io.ReaderHelper
  Doc: cx.ath.choisnet.xml.impl.SAXErrorHandlerImpl
  Doc: cx.ath.choisnet.xml.XMLBuilder
  Doc: cx.ath.choisnet.xml.XMLDOMTools
  Doc: cx.ath.choisnet.xml.XMLFileParser
  Doc: cx.ath.choisnet.xml.XMLParser
  Doc: cx.ath.choisnet.xml.XMLParserErrorHandler
  Doc: cx.ath.choisnet.xml.XMLParserException
  Fix: cx.ath.choisnet.xml.impl.XMLFileParserDOMImpl
  Add: cx.ath.choisnet.xml.impl.XMLParserDOMImpl
  Add: cx.ath.choisnet.xml.XMLURLParser
  Deprecated: cx.ath.choisnet.xml.impl.XMLFileParserDOM2Impl
  Deprecated: cx.ath.choisnet.xml.impl.XMLParserDOM2Impl
  Add: cx.ath.choisnet.xml.impl.XMLURLParserDOMImpl
---------------------------------------------------------------------
04.00 Maven version
---------------------------------------------------------------------
03a.38
  Fix: potential problem with threads
    cx.ath.choisnet.util.datetime.BasicDate
    cx.ath.choisnet.util.datetime.BasicTime
  Add: cx.ath.choisnet.util.datetime.BasicDate.incDay()
---------------------------------------------------------------------
03a.37
  Improve: cx.ath.choisnet.sql.ExportSQL (add schema name to export
  is optional; able to filter export)
---------------------------------------------------------------------
03a.36
  Prepare moving build to maven
---------------------------------------------------------------------
03a.35
  Fix: cx.ath.choisnet.sql.SQLTools (empty String)
---------------------------------------------------------------------
03a.34
  Add: cx.ath.choisnet.util.LinkedHashMapList
  Add: cx.ath.choisnet.util.AbstractMapCollection
  Add: cx.ath.choisnet.util.MapCollection
  Deprecated: cx.ath.choisnet.sql.mysql.MySQLTools#parseFieldValue
  Add: cx.ath.choisnet.sql.SQLTools
  Optimize: cx.ath.choisnet.sql.ExportSQL
  Some documentation
---------------------------------------------------------------------
03a.33
  Add: cx.ath.choisnet.util.SetWrapper
  Add: cx.ath.choisnet.util.MapKeyWrapper
  Modifications in:
    cx.ath.choisnet.sql.DataSourceFactory
    cx.ath.choisnet.sql.mysql.MySQLAdmin
    cx.ath.choisnet.sql.mysql.MyDataSourceFactory
    cx.ath.choisnet.sql.mysql.MySQLTools (TODO:Possible bug found
    here, need code review)
  Add: cx.ath.choisnet.sql.ExportSQL
---------------------------------------------------------------------
03a.32
  Documentation
---------------------------------------------------------------------
03a.30
  Minor fix in cx.ath.choisnet.lang.reflect.MappableHelper.toMap(Object object)
  Add some extra check in cx.ath.choisnet.io.HTMLWriter
  Major change: cx.ath.choisnet.lang.reflect.MappableHelperFactory
  is now an interface
  Add:  cx.ath.choisnet.lang.reflect.MappableHelperDefaultFactory
  Major change:  cx.ath.choisnet.lang.reflect.AbstractMappable
  Some improvement in cx.ath.choisnet.io.HTMLWriter
---------------------------------------------------------------------
03a.29
  minor fix, improve doc:
    - cx.ath.choisnet.sql.SimpleDataSource
    - cx.ath.choisnet.sql.SimpleQuery
    - cx.ath.choisnet.sql.SimpleUpdate
---------------------------------------------------------------------
03a.28
  Add: cx.ath.choisnet.sql.DatabaseMetaDataCollector
---------------------------------------------------------------------
03a.27
  Add: cx.ath.choisnet.util.iterator.iterable.IterableIterator
  Some JUnit Fix/Improve
---------------------------------------------------------------------
03a.26
  Remove Iterable interface on some Iterator (can't be sure to be able
  to reuse Iterator)
  Add package cx.ath.choisnet.util.iterator.iterable for
  Iterable-Iterator
  Add: cx.ath.choisnet.util.iterator.iterable.ArrayIterator
  Add: cx.ath.choisnet.util.iterator.iterable.BiIterator
---------------------------------------------------------------------
03a.25
  Add class cx.ath.choisnet.util.CancelRequestException
  Fix cancel handle in cx.ath.choisnet.util.duplicate.DuplicateFileCollector
  Add: method isCancel() to cx.ath.choisnet.util.duplicate.DigestEventListener
  Implement cancel from DigestEventListener in
    - cx.ath.choisnet.util.checksum.MessageDigestFile
    - cx.ath.choisnet.util.duplicate.DefaultDigestFileCollector
    - cx.ath.choisnet.util.duplicate.DuplicateFileCollector
---------------------------------------------------------------------
03a.24
  Move cx.ath.choisnet.net.PseudoPing to deprecated sources.
---------------------------------------------------------------------
03a.23
  Add: method toString(Charset) to cx.ath.choisnet.lang.ByteArrayBuilder
---------------------------------------------------------------------
03a.07
  Code review (improve documentation)
  Add: Annotation - alpha.cx.ath.choisnet.ToDo
  Add: Classes
    alpha.cx.ath.choisnet.io.AlternateDataStream
    alpha.cx.ath.choisnet.io.AlternateDataStreamTest
    alpha.cx.ath.choisnet.io.MSAlternateDataStreamDescription
    alpha.cx.ath.choisnet.io.MSAlternateDataStreamDescriptionEntry

  Since this code is base on reverse engineering of my lost sources
  library did some change.

  Deprecated Classes (Not use ?)
    cx.ath.choisnet.sql.SimpleQueryException

  Class moved to "alpha" package : (To be improve, or test, before release)
    alpha.cx.ath.choisnet.net.dhcp.DHCPMessage
    alpha.cx.ath.choisnet.net.dhcp.DHCPOptionEntry
    alpha.cx.ath.choisnet.net.dhcp.DHCPOptions
    alpha.cx.ath.choisnet.net.dhcp.DHCPParameters
    alpha.cx.ath.choisnet.net.dhcp.DHCPSimpleClient
    alpha.cx.ath.choisnet.net.dhcp.DHCPSocket

  Class moved to alpha package and Deprecated (Not sure)
    alpha.cx.ath.choisnet.net.PseudoPing


  Build: remove Deprecated classes from documentation
---------------------------------------------------------------------

---------------------------------------------------------------------
Known problems:
  - Maven migration is not completed.
---------------------------------------------------------------------
To add/improve:
  Create provide method to build Iterable-Iterator (this Iterator
  should be create using Iterable objects only)

---------------------------------------------------------------------
Future extensions:
---------------------------------------------------------------------
