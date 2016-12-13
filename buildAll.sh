#!/bin/bash
#
set +ex

[ -d .logs ] || mkdir .logs

mvn clean install | tee .logs/mvn-install.log
MVN_EXIT="${PIPESTATUS[0]}"
# MVN_EXIT="$?" - Not valid due to pipe

cat .logs/mvn-install.log | grep "warning: no description for" | sort | uniq > .logs/mvn-install-warn-no-desc.log
cat .logs/mvn-install.log | grep "warning: no @param for" | sort | uniq > .logs/mvn-install-warn-no-param.log
cat .logs/mvn-install.log | grep "warning: no @return" | sort | uniq > .logs/mvn-install-warn-no-return.log
cat .logs/mvn-install.log | grep "error: unknown tag:" | sort | uniq > .logs/mvn-install-error-unknown-tag.log
cat .logs/mvn-install.log | grep "error: exception not thrown:" | sort | uniq > .logs/mvn-install-error-not-thrown.log
cat .logs/mvn-install.log | grep "warning: empty" | sort | uniq > .logs/mvn-install-warning-empty.log
cat .logs/mvn-install.log | grep "error: reference not found" | sort | uniq > .logs/mvn-install-error-reference-not-found.log

cat .logs/mvn-install.log | grep -v "warning: no description for" \
  | grep -v "warning: no @param for" \
  | grep -v "warning: no @return" \
  | grep -v "error: unknown tag:" \
  | grep -v "error: exception not thrown:" \
  | grep -v "warning: empty" \
  | grep -v "error: reference not found" \
  > .logs/mvn-install-others.log

if [ ! "${MVN_EXIT}" -eq "0" ];
then
  echo "[ERROR] in ${MVN} ${MVN_PARAM}"
  exit 1
fi

pushd apps
./buildAllApps.sh | tee ../.logs/buildAllApps.log
popd

# Some cleanup
rm -fr /tmp/FolderTreeBuilderTest*
rm -fr /tmp/cchlib-test-io-*
rm -fr /tmp/content*-file*-*.png*
rm -fr /tmp/dup-file*.png
rm -fr /tmp/notduplicate-*
rm -fr /tmp/part-*.png
rm -fr /tmp/testCopyURLFile*

