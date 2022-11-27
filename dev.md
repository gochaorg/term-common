Сборка
==================

Смена версии
================

https://stackoverflow.com/questions/5726291/updating-version-numbers-of-modules-in-a-multi-module-maven-project

Use versions:set from the versions-maven plugin:

    mvn versions:set -DnewVersion=2.50.1-SNAPSHOT

It will adjust all pom versions, parent versions and dependency versions in a multi-module project.

If you made a mistake, do

    mvn versions:revert

afterwards, or

    mvn versions:commit
