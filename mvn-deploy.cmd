Call mvn-clean-package-javadoc.cmd
IF ERRORLEVEL 1 (
  Pause
  Goto :eof
  )


@Echo OFF
@Echo ------------------------------------------
@Echo TODO mvn deploy --errors
@Echo in each needed sub projets
@Echo (Not done by this batch)
@Echo mvn deploy -e --projects cchlib-core,cchlib-j2ee
