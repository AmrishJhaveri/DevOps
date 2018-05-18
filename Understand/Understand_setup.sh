#container name : jenkins

#make Understand folder
docker exec jenkins /bin/sh -c "cd /var/jenkins_home/workspace/Git_to_Gitlab; mkdir Understand"
echo "Created Understand folder"

#copy & extract Understand tar file
docker cp "./Understand-4.0.925-Linux-64bit.tgz" jenkins:/var/jenkins_home/workspace/Git_to_Gitlab/Understand
docker exec jenkins /bin/sh -c "cd /var/jenkins_home/workspace/Git_to_Gitlab/Understand; tar -xzf Understand-4.0.925-Linux-64bit.tgz; rm Understand-4.0.925-Linux-64bit.tgz"
echo "Understand extracted"

#license
docker cp "./users.txt" jenkins:/var/jenkins_home/workspace/Git_to_Gitlab/Understand/scitools/conf/license/
docker exec jenkins /bin/sh -c "cd /var/jenkins_home; mkdir .config; cd .config; mkdir SciTools"
docker cp "./Understand.conf" jenkins:/var/jenkins_home/.config/SciTools/
echo "Licensed Understand"