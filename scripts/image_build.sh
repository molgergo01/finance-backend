cd .. || exit 1

VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout) || exit 1

mvn clean install -DskipTests=true || exit 1

docker build -t molgergo01/finance-api:${VERSION} . || exit 1
docker push molgergo01/finance-api:${VERSION} || exit 1
echo "Successfully uploaded image to docker.io/molgergo01/finance-api"