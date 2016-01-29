#!/bin/bash

echo ""


echo ""
echo "Starting App..."
java -jar ../lib/ec-no-regrets-app-0.1.0.jar > ../logs/ec-no-regrets-app.log &
echo "Kicked off App."
echo ""

exit 0
