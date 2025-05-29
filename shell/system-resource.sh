#!/usr/bin/env bash

# 색상 정의
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[0;33m'; BLUE='\033[0;34m'; NC='\033[0m'

OS_TYPE="$(uname)"

print_date() {
  echo -e "${BLUE}==== 시스템 리소스 상태 조회 - $(date '+%Y-%m-%d %H:%M:%S') ====${NC}\n"
}

print_system_info() {
  echo -e "${BLUE}=== 시스템 정보 ===${NC}"
  echo -e "호스트명: $(hostname)"
  echo -n -e "운영체제: "
  if [[ "$OS_TYPE" == "Linux" ]]; then
    grep PRETTY_NAME /etc/os-release | cut -d= -f2 | tr -d '"'
    echo -e "가동 시간: $(uptime -p)"
  else  # Darwin Mac
    echo "$(sw_vers -productName) $(sw_vers -productVersion)"
    uptime | awk -F'( up |,)' '{print "가동 시간: " $2}'
  fi
  echo
}

print_cpu_info() {
  echo -e "${BLUE}=== CPU 정보 ===${NC}"
  if [[ "$OS_TYPE" == "Linux" ]]; then
    CORES=$(nproc)
    MODEL=$(grep 'model name' /proc/cpuinfo | head -1 | cut -d: -f2- | xargs)
    IDLE=$(top -bn1 | awk '/Cpu\(s\)/{print $8}' | sed 's/,//')
  else
    CORES=$(sysctl -n hw.ncpu)
    MODEL=$(sysctl -n machdep.cpu.brand_string)
    IDLE=$(top -l 1 | awk -F',' '/CPU usage/ {gsub(/%/,"",$3); print $3}')
  fi
  USAGE=$(printf "%.1f" "$(echo "100 - $IDLE" | bc)")
  echo -e "CPU 코어 수: $CORES"
  echo -e "CPU 모델: $MODEL"
  echo -n "CPU 사용량: "
  if (( $(echo "$USAGE > 80" | bc -l) )); then
    echo -e "${RED}${USAGE}% (높음)${NC}"
  elif (( $(echo "$USAGE > 50" | bc -l) )); then
    echo -e "${YELLOW}${USAGE}% (중간)${NC}"
  else
    echo -e "${GREEN}${USAGE}% (낮음)${NC}"
  fi
  echo
}

print_memory_info() {
  echo -e "${BLUE}=== 메모리 정보 ===${NC}"
  if [[ "$OS_TYPE" == "Linux" ]]; then
    read total used free shared buff cache available < <(free -m | awk 'NR==2{print $2, $3, $4, $5, $6, $7, $7}')
    usage_pct=$(printf "%.1f" "$(echo "$used/$total*100" | bc -l)")
  else # mac
    total_bytes=$(sysctl -n hw.memsize)
    total=$(( total_bytes/1024/1024 ))   # MB
    pagesize=$(vm_stat | awk '/page size of/{print $8}')
    free_pages=$(vm_stat | awk '/Pages free/{print $3}' | tr -d '.')
    inactive_pages=$(vm_stat | awk '/Pages inactive/{print $3}' | tr -d '.')
    free=$(( (free_pages + inactive_pages) * pagesize / 1024 / 1024 ))
    used=$(( total - free ))
    usage_pct=$(printf "%.1f" "$(echo "$used/$total*100" | bc -l)")
  fi

  echo -e "총 메모리: ${total} MB"
  echo -n -e "메모리 사용량: ${used} MB (${usage_pct}%) - "
  if (( $(echo "$usage_pct > 80" | bc -l) )); then
    echo -e "${RED}높음${NC}"
  elif (( $(echo "$usage_pct > 60" | bc -l) )); then
    echo -e "${YELLOW}중간${NC}"
  else
    echo -e "${GREEN}낮음${NC}"
  fi
  echo -e "사용 가능한 메모리: ${free} MB\n"
}

print_disk_info() {
  echo -e "${BLUE}=== 디스크 사용량 ===${NC}"
  df -h | awk 'NR==1 || /^\/dev/' | while read fs size used avail usep mount; do
    printf "%-20s %6s %6s %6s " "$fs" "$size" "$used" "$avail"
    pct=${usep%\%}
    if (( pct > 90 )); then
      printf "${RED}%4s%%${NC} " "$pct"
    elif (( pct > 70 )); then
      printf "${YELLOW}%4s%%${NC} " "$pct"
    else
      printf "${GREEN}%4s%%${NC} " "$pct"
    fi
    echo " $mount"
  done
  echo
}

print_top_cpu_processes() {
  echo -e "${BLUE}=== 상위 CPU 사용 프로세스 (Top 5) ===${NC}"
  if [[ "$OS_TYPE" == "Linux" ]]; then
    ps -eo user,pid,%cpu,comm --sort=-%cpu | head -n 6
  else # macOS
    ps -A -o user,pid,%cpu,comm | sort -k3 -nr | head -n 6
  fi
  echo
}

print_top_memory_processes() {
  echo -e "${BLUE}=== 상위 메모리 사용 프로세스 (Top 5) ===${NC}"
  if [[ "$OS_TYPE" == "Linux" ]]; then
    ps -eo user,pid,%mem,comm --sort=-%mem | head -n 6
  else # macOS
    ps -A -o user,pid,%mem,comm | sort -k3 -nr | head -n 6
  fi
  echo
}


print_network_info() {
  echo -e "${BLUE}=== 네트워크 연결 상태 ===${NC}"
  echo -e "활성화된 ESTABLISHED 연결 수: $(netstat -an | grep ESTABLISHED | wc -l)"
  echo -e "LISTEN 포트:"
  netstat -an | grep LISTEN
  echo
}

check_spring_app() {
  echo -e "${BLUE}=== Runimo 애플리케이션 상태 ===${NC}"
  PID=$(pgrep -f "Runimo.jar" | head -1)
  if [[ -n "$PID" ]]; then
    echo -e "Runimo 상태: ${GREEN}실행 중 (PID: $PID)${NC}"
  else
    echo -e "Runimo 상태: ${RED}미실행${NC}"
  fi
  echo
}

check_docker_containers() {
  echo -e "${BLUE}=== Docker 컨테이너 상태 ===${NC}"
  if command -v docker &> /dev/null; then
    COUNT=$(docker ps -q | wc -l)
    echo -e "실행 중인 컨테이너 수: ${GREEN}$COUNT${NC}"
    docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"
  else
    echo -e "${YELLOW}Docker가 설치되어 있지 않습니다.${NC}"
  fi
  echo
}

# 전체 출력
print_all() {
  clear
  print_date
  print_system_info
  print_cpu_info
  print_memory_info
  print_disk_info
  print_top_cpu_processes
  print_top_memory_processes
  check_spring_app
  check_docker_containers
}

case "$1" in
  cpu)    print_date; print_cpu_info; print_top_cpu_processes ;;
  memory) print_date; print_memory_info; print_top_memory_processes ;;
  disk)   print_date; print_disk_info ;;
  app)    print_date; check_spring_app ;;
  docker) print_date; check_docker_containers ;;
  *)      print_all ;;
esac

exit 0
