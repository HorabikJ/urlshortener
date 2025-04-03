#!/bin/bash

# Define default values and help function
function show_usage {
    echo "Usage: $0 [options]"
    echo "Options:"
    echo "  --app-base-url                        Base URL of the application"
    echo "  --keycloak-internal-base-url          Internal base URL for Keycloak"
    echo "  --keycloak-external-base-url          External base URL for Keycloak"
    echo "  --urlshortener-client-secret          Client secret for URL shortener"
    echo "  --gmail-app-password                  Gmail app password"
    echo "  --help                                Show this help message"
    exit 1
}

# Initialize variables
appBaseUrl=""
keycloakInternalBaseUrl=""
keycloakExternalBaseUrl=""
urlshortenerClientSecret=""
gmailAppPassword=""

# Parse named parameters
while [[ $# -gt 0 ]]; do
    case "$1" in
        --app-base-url)
            appBaseUrl="$2"
            shift 2
            ;;
        --keycloak-internal-base-url)
            keycloakInternalBaseUrl="$2"
            shift 2
            ;;
        --keycloak-external-base-url)
            keycloakExternalBaseUrl="$2"
            shift 2
            ;;
        --urlshortener-client-secret)
            urlshortenerClientSecret="$2"
            shift 2
            ;;
        --gmail-app-password)
            gmailAppPassword="$2"
            shift 2
            ;;
        --help)
            show_usage
            ;;
        *)
            echo "Unknown parameter: $1"
            show_usage
            ;;
    esac
done

# Check if required parameters are provided
if [[ -z "$appBaseUrl" || -z "$keycloakInternalBaseUrl" || -z "$keycloakExternalBaseUrl" || 
      -z "$urlshortenerClientSecret" || -z "$gmailAppPassword" ]]; then
    echo "Error: Missing required parameters"
    show_usage
fi

# Copy template file
rm -f ./realm/*.json
cp ./urlshortener-keycloak-realm-template.json ./realm/urlshortener-keycloak-realm.json

# this empty '' in below sed commands is applicable only for macOS, remove it in linux
sed -i '' "s|<APP-BASE-URL>|$appBaseUrl|g" ./realm/urlshortener-keycloak-realm.json
sed -i '' "s|<KEYCLOAK-INTERNAL-BASE-URL>|$keycloakInternalBaseUrl|g" ./realm/urlshortener-keycloak-realm.json
sed -i '' "s|<KEYCLOAK-EXTERNAL-BASE-URL>|$keycloakExternalBaseUrl|g" ./realm/urlshortener-keycloak-realm.json
sed -i '' "s|<URL-SHORTENER-CLIENT-SECRET>|$urlshortenerClientSecret|g" ./realm/urlshortener-keycloak-realm.json
sed -i '' "s|<GMAIL-APP-PASSWORD>|$gmailAppPassword|g" ./realm/urlshortener-keycloak-realm.json

echo "Configuration completed successfully"
exit 0
