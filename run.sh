#!/bin/bash

set -ex

sbt -warn setupPublishCore generateBuildCharacterPropertiesFile publishLocal
STARR=`cat buildcharacter.properties | grep ^maven.version.number | cut -d= -f2` && echo $STARR
sbt -Dstarr.version=$STARR -warn setupValidateTest test:compile info testAll
