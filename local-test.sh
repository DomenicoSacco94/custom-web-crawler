#!/bin/bash

# Default port number
PORT=45000

# Parse command line arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --serve-port) PORT="$2"; shift ;;
        *) echo "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

# Get the IP address of the host machine
HOST_IP=$(hostname -I | awk '{print $1}')

# Navigate to the directory containing the files
cd src/test/resources/testfiles || exit

# Start a simple HTTP server on the specified port
python3 -m http.server "$PORT" &
SERVER_PID=$!

# Function to shut down the server
shutdown_server() {
    echo "Shutting down server..."
    kill $SERVER_PID
}

# Trap the SIGINT signal to ensure the server is shut down
trap shutdown_server SIGINT

# Wait for the server to start
sleep 2

# Generate the URLs
URLS=()
for file in *; do
    URLS+=("http://$HOST_IP:$PORT/$file")
done

# Echo the URLs as a JSON payload
echo "{"
echo "  \"urls\": ["
for i in "${!URLS[@]}"; do
    if [ $i -eq $((${#URLS[@]} - 1)) ]; then
        echo "    \"${URLS[$i]}\""
    else
        echo "    \"${URLS[$i]}\","
    fi
done
echo "  ]"
echo "}"

# Keep the script running to keep the server alive
wait $SERVER_PID