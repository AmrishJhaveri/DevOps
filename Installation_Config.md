# Development Operation(DevOps)
This is a guide to build this automation from scratch.
## Prerequisites
----------
 - Docker version 18.01.0-ce and above
 - JDK 8

## Docker Basics
----------
Refer [this](https://docs.docker.com/get-started/) to become aware about the basic Docker terminology and commands. Some important commands we will be using are described below.
###  Docker Machine IP([link](https://docs.docker.com/machine/reference/ip/))
Get the IP address of docker machines.

    docker-machine ip

### Docker Logs ([link](https://docs.docker.com/engine/reference/commandline/logs/#usage))
Fetch the logs of a container.

    docker logs <CONTAINER_ID> --tail 500 -f
### Docker Exec([link](https://docs.docker.com/engine/reference/commandline/exec/#run-docker-exec-on-a-running-container))
Run a command in a running container.

    docker exec -it <CONTAINER-NAME/ID> bash

### Docker Copy([link](https://docs.docker.com/engine/reference/commandline/cp/))
Copy files/folders between a container and the local file-system.

    docker cp <SOURCE_PATH> <CONTAINER_NAME/ID>:<DESTINATION_PATH>

### Docker Inspect

## 1] Jenkins
----------
Jenkins is an open source automation server.

### Steps for installation on Docker:

Pull the fresh docker image of Jenkins using the below command:
(URL: [Jenkins](https://hub.docker.com/r/jenkins/jenkins/))

    docker pull jenkins/jenkins:lts

This will download the Jenkins docker image on your system or directly run the below command.
Run the following command to start a container for the above Jenkins image:

    docker run --detach --name jenkins  -p 9080:8080 -p 50000:50000 jenkins/jenkins:lts

Since you are doing this for the first time, you will require a password provided by Jenkins to set up the account from the browser. To find the password use the command:

    docker logs jenkins --tail 1000 -f
Open the browser and access Jenkins by using the following URL.

    http://192.168.99.100:9080/
Please check the IP is the same as output from `docker-machine ip`.
Follow the setup of process. Make sure you install the plugins for Cobertura, Git, GitHub, Gitlab, Gradle, JaCoCo, Job DSL, Maven, SBT.
Setup the *admin* user.

### Jenkins Configuration
#### Gitlab User Credential Setup

 - Please setup Gitlab as per the instructions given below and create a root user.
 - Navigate to *Credentials* section displayed on the left hand-side of the Jenkins dashboard.
 - Click on the *System* sub-menu under Credentials on the left side.
 - Open the drop down of *Global credentials (unrestricted)* and select *Add Credentials*.![Creds](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1/raw/master/images/Creds.PNG)
 - Create new credentials of the kind *Username with password*.
 - Make the scope as *Global*
 - Insert the *Username* created on Gitlab i.e. root
 - Enter the *Password* for the user on Gitlab.
 - Leave the *ID* and *Description* field blank.
 - Once created open Global Credentials link and you will be able to the user which was created.
 - The *ID* field is generated which is used in the Jenkins JOB DSL plugin to generate Jenkins job with the credentials of the above user.

#### JDK Installation
Please make sure JDK v7 or above is already installed on your system.
This path will be automatically be taken by docker and provided to Jenkins container.

#### Maven Installation
For installing Maven on Jenkins follow the following steps:
- Navigate to `Global Tool Configuration` present inside `Manage Jenkins` section.
- Scroll down to find the `Maven` Section and click on `Maven Installations`.
- Give the installation a name so it can be referred in the Jenkins jobs.
![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1/raw/master/images/maven.JPG)


#### Environment Variables
For using the Understand binaries we have to provide the binary path to `UND` as environment variable. This can be configured as follows:
- Navigate to `Configure System` present inside `Manage Jenkins` section.
- Under Global Properties, check `Environment Variables` and append the Understand path to the `und` script to the `PATH` variable:
e.g. : `/var/jenkins_home/workspace/Git_to_Gitlab/Understand/scitools/bin/linux64/`

![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1/raw/master/images/env_var.JPG)

### Jenkins Job

#### Creation of Job
- Select on `New Item` on the left hand side in the Jenkins Dashboard.
- Create a `Free Style` Job and name it `Git_to_Gitlab`.

#### Passing Parameters to the Job
- Under `General` tab present in the `Job Configure` Page check the option `This project is parameterized`.
- Fill the details as given in the image.
![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1/raw/master/images/param.JPG) 

#### 1. Shell Commands
- Under the Build Section, add a tab `Execute Shell` and enter the following commands:

		#Go to the Directory
    	cd CloneRepo
    
    	#Dowload the Github data to a json file
    	curl -H "Content-Type:application/json" "https://api.github.com/search/repositories?q=maven%20language:$LANGUAGE+license:mit+size:%3C=10000&type=Repositories&sort=stars&order=desc" > java-full-data.json
    	#curl -H "Content-Type:application/json" "https://api.github.com/search/repositories?q=language:$LANGUAGE+license:mit+size:%3C=10000&type=Repositories&sort=stars&order=desc" > java-full-data.json
    
    	#Run the jar on the above json and create 2 other json files with required data and clone the repos
    	# The first parameter can be changed to -1 to download all projects or change to a number less than 30.
    	java -jar CloneRepo.jar 3 ./java-full-data.json ../output-project-names.json ../output-repo-issue-links.json ../
    
    	cd ..
    
    	#Loop through the json file which has the project names
    	while read line; do
    	
    		echo $line
    
    		# Create Gitlab repo remotely
    		curl -H "Content-Type:application/json" http://172.17.0.3:30080/api/v4/projects?private_token=iTagjb7gZiq3ptz_U-X3 -d "{ \"name\": \"$line\" }"
    	
    		cd $line
    	
    		#Change the origin URL from github to local gitlab
    		git remote set-url origin http://oauth2:iTagjb7gZiq3ptz_U-X3@172.17.0.3:30080/root/$line.git
    	
    		git remote -v
    	
    		#Push the proejct to the new origin
    		git push -u origin --all
    	
    		cd ..
    
    	
    	done < output-project-names.json
    	ls -ltr

#### 2. Jenkins Job DSL Plugin
This will be used to create Jenkins Jobs dynamically after the repositories are created by the above code on Gitlab.

	
    import static groovy.io.FileType.FILES
    import hudson.model.*
    
    //File which has the list of Github Project names which were pulled.  
    def theInfoName = '/var/jenkins_home/workspace/Git_to_Gitlab/output-project-names.json'
    
    File theInfoFile = new File(theInfoName)
    
    //Priting out the project names 
    theInfoFile.eachLine {
     line ->
      if (line.trim()) {
       println line
      }
    
    //Iterate over the files in the clone directory and check for pom.xml(Maven project).
    // If Maven Project than create the Jenkins Job
    // This logic can be extended to other build type projects  
     new File("/var/jenkins_home/workspace/Git_to_Gitlab/$line")
      .eachFileMatch(~/pom.xml/) {
       file -> println file.getName()
       job("DSL-$line") {
    
    scm {
     gitSCM {
      userRemoteConfigs {
    
       userRemoteConfig {
    	//Gitlab URL
    url("http://root@172.17.0.3:30080/root/" + line + ".git")
		//Id for the credentials stored
    credentialsId("fc80c669-76a2-49d8-affc-1abef8191249")
    name("origin")
    refspec("")
       }
      }
      branches {
       branchSpec {
    name("*/master")
       }
      }
      doGenerateSubmoduleConfigurations(false)
      browser {}
      gitTool("")
    
     }
    }
    //Setting up trigger for ever 15 minutes
    triggers {
     scm('H/15 * * * *')
    }
    steps {
     
	//Setting the maven commands      
     maven {
		// Call the maven installed on Jenkins    
      mavenInstallation("Jenkins_maven")
		// execute maven goals with Jacoco Coverage
      goals("-Djacoco.destFile=./coverage/jacoco.exec clean org.jacoco:jacoco-maven-plugin:0.8.0:prepare-agent install")
       //properties(skipTests: true)
    
     }
    	//shell commands for Understand reports
     shell('rm -rf ../Git_to_Gitlab/GitRetest/DSL-' + line + '; mkdir ../Git_to_Gitlab/GitRetest/DSL-' + line + '; ' +
      "und create -db ../Git_to_Gitlab/GitRetest/DSL-" + line + "/" + line + " -languages java web; " +
      "und -db ../Git_to_Gitlab/GitRetest/DSL-" + line + "/" + line + ".udb add ./ ;" +
      "und -db ../Git_to_Gitlab/GitRetest/DSL-" + line + "/" + line + ".udb analyze ;" +
      "und -db ../Git_to_Gitlab/GitRetest/DSL-" + line + "/" + line + ".udb report ;" +
      "und -db ../Git_to_Gitlab/GitRetest/DSL-" + line + "/" + line + ".udb export -dependencies -format longnoroot file csv " + "../Git_to_Gitlab/GitRetest/DSL-" + line + "/" + line + ".csv" + ";" +
      'java -jar ../Git_to_Gitlab/GitRetest/GitRetest.jar ./ ../Git_to_Gitlab/GitRetest/DSL-' + line + '/output_changed_files.txt ../Git_to_Gitlab/GitRetest/DSL-' + line + '/' + line + '.csv ; ' +
      "echo '**********************Changed File List & Dependencies***********************'  ; " +
      'cat ../Git_to_Gitlab/GitRetest/DSL-' + line + '/output_changed_files.txt'
     )
    }
    
		//Publish the Jacoco Report
    publishers {
    
     jacocoCodeCoverage {
      execPattern("**/*.exec")
     }
    }
       }
      }
    /*
     new File("/var/jenkins_home/workspace/Git_to_Gitlab/$line")
      .eachFileMatch(~/.*.gradle/) {
       file -> println file.getName()
       job("DSL-$line") {
    
    triggers {
     scm('H/15 * * * *')
    }
    steps {
     shell('git clone http://oauth2:iTagjb7gZiq3ptz_U-X3@172.17.0.3:30080/root/' + line + '.git;cd ' + line)
     gradle 'assemble'
    }
    
       }
      }
    
     new File("/var/jenkins_home/workspace/Git_to_Gitlab/$line")
      .eachFileMatch(~/.*b*.sbt/) {
       file -> println file.getName()
       job("DSL-$line") {
    
    triggers {
     scm('H/15 * * * *')
    }
    steps {
     shell('git clone http://oauth2:iTagjb7gZiq3ptz_U-X3@172.17.0.3:30080/root/' + line + '.git;cd ' + line)
     sbt('sbt-jenkins', null, '-Dsbt.log.noformat=true', null, null)
    }
    
       }
      }*/
    }
	
#### 3. Shell Commands
These commands are for removal of the cloned repositories from Jenkins since they are pushed to the local Gitlab server.
These will also download the issues of the repositories and push it to Redis storage.

    #Removal of cloned repositories from Jenkins
    while read line; do   
    
    	rm -rf $line
    	
    done < output-project-names.json
    
    #Downloading the issues of the repos downloaded and storing to Redis
    while read line; do   
    
    	curl -H "Content-Type:application/json" $line > current-repo-issues.json
    
    	java -jar ./RedisStorage/RedisStorage.jar "172.17.0.4" "./current-repo-issues.json" $line
    	
    done < output-repo-issue-links.json
    
    rm current-repo-issues.json


## 2] Gitlab
----------
Gitlab is a fully featured Git Server which can be installed on your system.

### Steps for Installation on Docker:
Run the following command for downloading the Gitlab image and starting a container.

    docker run --detach --name gitlab \
    	--hostname gitlab.example.com \
    	--publish 30080:30080 \
        --publish 30022:22 \
    	--env GITLAB_OMNIBUS_CONFIG="external_url 'http://gitlab.example.com:30080'; gitlab_rails['gitlab_shell_ssh_port']=30022;" \
    gitlab/gitlab-ce:9.1.0-ce.0
Gitlab will take some time to start. Open the browser and access Gitlab by using the following URL.

    http://192.168.99.100:30080/
Please check the IP is the same as output from `docker-machine ip`. If you see HTTP 500 Error than wait for some time and check the logs by using

    docker logs gitlab --tail 1000 -f

Setup root user and login to Gitlab.

### Configuration

#### Access Token for Jenkins
- Navigate to top right dropdown menu and click on `Settings`. Open `Access Tokens` tab.
![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1/raw/master/images/gitlab.JPG)
- Enter the name and select the 2 check boxes and create the access token.
- Copy the token and configure with Jenkins as described above.


----------

## 3] Redis

### Steps for installation on Docker

    docker run -d --name my-redis -p 6379:6379 redis

### Checking Data on Server 
The data on Redis is stored in the form of a Hash data structure of Redis.
Follow the steps below to see all the data:

- Login to the running redis container by using the following command:
	
	`docker exec -it my-redis bash`


- Run the Redis client by executing the below command:

	`redis-cli`

- Run `HGETALL "repo"`
- This should display the field and the value corresponding the "repo" key.


The command to set the key field value to Redis Hash data structure is:

    HSET "key" "field" "value"

We have used Jedis API which is a Java API for inserting to Redis Database.

We can use the following command to see all the fields and value corresponding to a particular key:

    HGETALL "repo"
Refer this [link](https://redis.io/commands/hgetall).

----------

## 4] Understand

### Steps for installation on Docker

1. Go to path `/var/jenkins_home/workspace/Git_to_Gitlab` and make new directory "Understand"
2. Copy `Understand-4.0.925-Linux-64bit.tgz` from local drive to above created Understand directory
3. Extract this file using command `tar -xzf Understand-4.0.925-Linux-64bit.tgz`. This may take more than a minute. 
4. When done, remove this file using `rm Understand-4.0.925-Linux-64bit.tgz`
5. Now we need to setup the license for Understand. 		
	* Go to this path: `/var/jenkins_home/workspace/Git_to_Gitlab/Understand/scitools/conf/license/` 
	* Make a new file "users.txt" with the following content:
		> jenkins <license-key> # any
	
		Use `whoami` command to verify that the current username is jenkins. 
		
		This step may require root permissions for file editing. 
		
		Hence the command `docker exec -it -u root <container-name>` must be used to login as root.
		
		To install "vim" for file editing, execute 
		> `apt-get update` 
		
		and then 
		>`apt-get install vim` 
		
		while logged in as root user.
	
	* Now go to this path:  `/var/jenkins_home`
	* Create a new directory `.config`, then create another directory `SciTools` inside `.config`. 
	* This step may again require root permissions. Follow same steps as above for logging through root. <br>Now, in `/var/jenkins_home/.config/SciTools` create a new file `Understand.conf` with the following content:
			    
   			[General]
   			UserText=<registered-email>
6. Set the environment variable for Understand by:
	`export PATH=$PATH:/var/jenkins_home/workspace/Git_to_Gitlab/Understand/scitools/bin/linux64/`
	
	This is the path where the understand executable rests.
7. Verify the installation by typing `und` on the container shell.
	
	You should the und shell invoked:


    	jenkins@764da6795a64:/$ und
    	
    	Welcome to und. Type "help" for a list of commands. "quit" to quit
    	
    	und>
    		
###

---

## JARs

### Cloning Repo with JGIT
This is a Maven based Java Project which uses JGit API to clone the repositories from Github.
Project present at [https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/MyJGitProject/](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/MyJGitProject/) 

### Redis Storage with Jedis
This is a Maven based Java Project which uses the Jedis API to push and retrieve data on the Redis Server Database.
Project present at [https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/Redis/](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/Redis/)

### Retest Components with Understand & JGit
This is a Maven based Java Project which uses the JGit API to find the files changed in the previous commit and uses the data provided by Understand API to find the file components which need to be retested. 

Project present at [https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/GitRetest/](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/GitRetest/)