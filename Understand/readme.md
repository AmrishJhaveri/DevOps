## Understand
Understand is a cross-platform, multi-language tool we use for static code analysis.

### Prerequisites for installation on Docker:
* Must have license key and corresponding registered email address for Understand 4.0
- Download the following installation: [Understand 4.0 Build 925](builds.scitools.com/all_builds/b925/Understand/Understand-4.0.925-Linux-64bit.tgz)
- Jenkins container must be created and workspace/Git_to_Gitlab directory must exist

### und commands for shell

Functionality| Command
- | -
Create understand project	| `und create -db <project-name> -languages <space-separated languages>`
Add	files/directory	| `und -db <path-to-udb-file>\<project-name>.udb add <path-to-app-proj-dir>`
Analyze	project	| `und -db <path-to-udb-file>\<project-name>.udb analyze`
Generate report	| `und -db <path-to-udb-file>\<project-name>.udb report`
Generate dependency csv report	| `und -db <path-to-udb-file>\<project-name>.udb export -dependencies -format longnoroot file csv <file-name>.csv`

### Steps for Installation on Docker

There are 2 ways to install Understand on Docker:

+ Run und-setup.sh script in docker. This requires following files to be present at docker's current location:
	+ Understand-4.0.925-Linux-64bit.tgz
	+ users.txt
		+ This file should have the following content format
		 `jenkins <license-key> # any`
		+ `jenkins` is the login user. 
		+ `<license-key>` is the license received from SciTols-Understand registered with your email
	+ Understand.conf
		
		>`[General]`
		>
		>`UserText=<registered-email>`

+ Manually setup Understand in jenkins container in Docker
	Refer to [Installatioin_Config.md](https://bitbucket.org/ajhave5/amrish_jhaveri_chinmay_gangal_hw1_cs540/src/master/Installation_Config.md) for a detailed stepwise instructions on installing Understand in Jenkins container on Docker from scratch		
