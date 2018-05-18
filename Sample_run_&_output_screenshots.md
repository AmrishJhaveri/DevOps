# Sample Run & Output Screenshots #

1. Opening Jenkins Home at url `<docker-ip>:9080`

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_1.JPG)

2.	Open `Git_to_Gitlab` Jenkins Job.
	
	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_2.JPG)
	
3.	Click on `Build with Parameters` and enter language `Java` by default.
	
	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_3.JPG)

4.	Open the build (latest one,may require to refresh the page). You should see 	the generated jobs.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_4.JPG)

5.	Open the Console output from the left side.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_5.JPG)

6.	The Github API Url, cloning 3 repos and the time taken for each repo to be 	cloned.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_6.jpg)

7.	Creating Gitlab Repo with Curl, JSON response from Gitlab API and changing 	the remote of the repo cloned to point to new empty Gitlab repo.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_7.jpg)

8.	Push the files to local Gitlab Repo and repeat the process for other 	repositories.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_8.jpg)

9.	Creating Jenkins Jobs with Job DSL plugin if it is a Maven Project, name of 	the Jenkins Jobs generated, remove the folder from Jenkins after it is pushed 	to Gitlab.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_9.jpg)

10.	Download the issue list of the repo using Github Issues API, invoke the Redis 	Storage Jar which connects to Redis Database , displays the previous data and 	pushes the new issues list.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_10.jpg)

11.	 Repeat this process for all the repositories.

12.	Login to Gitlab at URL `<docker-ip>:30080` with `root` user and `123456789` 	as 	password.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_12.jpg)

13.	Open a project and see the history which is same as it was on Github.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_13.jpg)

14.	Login to the Redis contianer via `docker exec -it my-redis bash` and open 	Redis Client via the command `redis-cli`.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_14.jpg)

15.	Execute the command `HGETALL "repo"` to see all the fields and values of data 	pushed to Redis Db via Jenkins.It shown in the order.
	1.	field1
	2.	value1
	3.	field2
	4.	value2
	(likewise)

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_15.jpg)

16.	Build the project generated in Jenkins like DSL-maven-plugins. Jacoco report 	shown on the right of project home page.
	
	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_16.jpg)

17.	Click on the report to see the detailed result of the coverage.
	
	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_17.jpg)

18.	Login to Jenkins container via `docker exec -it 	jenkins bash` . Navigate 	to `/var/jenkins_home/workspace/Git_to_Gitlab/	GitRetest/`. Find different 	folders with names `DSL-<project_names>`. Open 	the folder `DSL-maven-	plugins`. The file `maven-plugins.csv` contains all the 	dependencies of 	the project. This csv is passed to a jar which filters based 	on the last 	files committed. Final list of dependencies found in file 	`output_changed_files.txt`.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_18.jpg)

19.	Open the last project build from Jenkins GUI. Open the Console Output 	section. Scroll down at the end and find the section of **`Changed File List 	& 	Dependencies`**
	If the .java files are present in the last commit, it will find the dependencies of that file and show it in the output.

	![](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/raw/master/images/sr_19.jpg)