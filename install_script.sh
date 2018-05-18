#Installation script for getting Jenkins, Gitlab and Redis

#Invoke the Jenkins Installation script inside the Jenkins folder of DockerImages
echo "Commencing Jenkins setup"
./DockerImages/Jenkins/Jenkins_setup.sh
echo "Jenkins setup successful"

#Invoke the Gitlab Innstallation script inside the Gitlab folder of DockerImages
echo "Commencing Gitlab setup"
./DockerImages/Gitlab/Gitlab_setup.sh
echo "Gitlab setup successful"

#Execute commands for Redis setup
echo "Commencing Redis setup"
docker run -d --name my-redis -p 6379:6379 redis
echo "Redis setup successful"
