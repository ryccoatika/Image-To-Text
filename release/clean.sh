#!/bin/sh

# Delete Release key
rm -f release/release.jks

# Delete Play Store key
rm -f release/play-account.json

# Delete Google Services key
rm -f app/google-services.json
