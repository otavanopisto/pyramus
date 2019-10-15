if [ $TRAVIS_PULL_REQUEST == false ]; then
  echo Preparing to deploy
  python travis-prepare-maven.py;
  mvn deploy --settings ~/.m2/mySettings.xml;
fi;