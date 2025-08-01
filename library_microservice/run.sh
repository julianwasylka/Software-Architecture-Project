if [[ $1 =~ ^-?[0-9]+$ ]]; then
  # It's a valid integer (including negatives)
  if (( $1 < 1 )); then
    ./shutdown.sh
    exit
  fi
else
  echo "Error: Argument is not a valid number"
  exit
fi

db_base_name="books_db_"
db_base_port=5432

latest_i=$(docker ps --filter "name=^${db_base_name}[1-9][0-9]*$" --format "{{.Names}}" | sort -V | tail -n 1 | grep -oE '[0-9]+$')
if((latest_i > $1)); then
  for ((i = $1+1; i <= latest_i; i++)); do
    db_name="$db_base_name${i}"
    db_port=$(($db_base_port + i))

    docker stop ${db_name}
    docker rm ${db_name}

    echo "Stoped ${db_name} on port ${db_port}"
  done

else
  if ((latest_i < $1)); then
    for ((i = 1; i <= $1; i++)); do
      db_name="$db_base_name${i}"
      db_port=$((db_base_port + i))

      docker run -d \
        --name "${db_name}" \
        --network lms_overlay_attachable_network \
        -e POSTGRES_USER=postgres \
        -e POSTGRES_PASSWORD=password \
        -p "${db_port}:5432" \
        postgres

      echo "Started ${db_name} on port ${db_port}"
    done
  fi
fi

echo Running $1 instances of lmslibrary, each connecting to a different postgres DBMS

if docker service ls --filter "name=lmslibrary" --format "{{.Name}}" | grep -q "^lmslibrary$"; then
  docker service scale lmslibrary=$1
else
  docker service create -d \
    --name lmslibrary \
    --env SPRING_PROFILES_ACTIVE=bootstrap \
    --env spring.datasource.url=jdbc:postgresql://books_db_{{.Task.Slot}}:5432/postgres \
    --env spring.datasource.username=postgres \
    --env spring.datasource.password=password \
    --env spring.rabbitmq.host=rabbitmq \
    --publish 8087:8080 \
    --network lms_overlay_attachable_network \
    lmslibrary:latest

  docker service scale lmslibrary=$1
fi