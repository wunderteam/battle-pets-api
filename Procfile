web: target/universal/stage/bin/koalify -Dhttp.port=${PORT} -Dnewrelic.environment=${ENVIRONMENT} -J-javaagent:/app/target/universal/stage/lib/com.newrelic.agent.java.newrelic-agent-3.35.2.jar -J-Dnewrelic.config.file=conf/newrelic.yml

migration: ./bin/migrateDB $JDBC_DATABASE_URL
