cd ../charts/finance-api || exit 1

echo "Namespace: "
read -r namespace || exit 1

helm install finance-api . -n $namespace || exit 1
