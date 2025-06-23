#!/bin/bash
set -e

# Default values
PROJECT_DIR="${PROJECT_DIR:-$(pwd)/..}"

# Export environment variables
export PROJECT_DIR

show_info() {
    echo "Project directory: $PROJECT_DIR"
    echo ""
}

case "$1" in
    "start")
        echo "Starting Shortly..."
        show_info
        ansible-playbook -i hosts deploy.yml
        ;;
    "stop")
        echo "Stopping Shortly..."
        show_info
        ansible-playbook -i hosts shutdown.yml
        ;;
    "restart")
        echo "Restarting Shortly..."
        show_info
        echo "Stopping services..."
        ansible-playbook -i hosts shutdown.yml
        echo ""
        echo "Waiting 5 seconds..."
        sleep 5
        echo ""
        echo "Starting services..."
        ansible-playbook -i hosts deploy.yml
        ;;
    "status")
        echo "Checking Shortly's status..."
        show_info
        cd "$PROJECT_DIR" && docker compose ps
        echo ""
        echo "  Expected URLs (if running):"
        echo "  MOD-UI: http://localhost:3000"
        echo "  MOD-URL: http://localhost:8080"
        echo "  MOD-Analytics: http://localhost:8081"
        echo "  Grafana: http://localhost:3001"
        echo "  Prometheus: http://localhost:9090"
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status}"
        echo ""
        echo "Commands:"
        echo "  start    - Deploy and start all services"
        echo "  stop     - Stop all services (preserve data)"
        echo "  restart  - Stop then start services"
        echo "  status   - Show current container status"
        echo ""
        echo "Environment variables:"
        echo "  PROJECT_DIR - Project directory (default: ../)"
        echo ""
        echo "Examples:"
        echo "  $0 start"
        echo "  PROJECT_DIR=/custom/path $0 start"
        exit 1
        ;;
esac