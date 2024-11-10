FROM openjdk:21-jdk-slim

# Install Python and pip, and also ensure python3-venv is installed
RUN apt-get update && apt-get install -y python3 python3-pip python3-venv

# Create a virtual environment for Python packages
RUN python3 -m venv /opt/venv

# Install required Python packages inside the virtual environment
RUN /opt/venv/bin/pip install pandas numpy openai==0.28.0 tqdm matplotlib python-dotenv scipy dask

# Copy your JAR file and .env file
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY .env .env

# Set the timezone and run the application
ENTRYPOINT ["/bin/sh", "-c", ". /opt/venv/bin/activate && java -jar -Duser.timezone=Asia/Seoul /app.jar"]
