package cs540.hw1.cs540.hw1;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {

	private static String INPUT_JSON_READ_FILE = "./src/main/resources/java-full-data.json";

	private static String OUTPUT_PROJECT_NAMES = "./src/main/resources/output-project-names.json";

	private static String OUTPUT_REPO_LINKS = "./src/main/resources/output-repo-issue-links.json";
	


	private static String OUTPUT_CLONE_DIR = "./Testing_output/";
	
	//private static String OUTPUT_PROJECT_CHANGES="";

	public static void main(String[] args) throws IOException, GitAPIException, ParseException {

		// find all the projects from the URL.(Java)
		// write to a file the project names(Java)
		// this will be used to create Gitlab repos(Shell Script)
		// change remote for all the projects and then push them to the gitlab
		// repo(Shell Script)

		int noOfProjects = 1;

		

		if (args.length > 0) {
			noOfProjects=Integer.parseInt(args[0]);
			INPUT_JSON_READ_FILE = args[1];
			OUTPUT_PROJECT_NAMES = args[2];
			OUTPUT_REPO_LINKS = args[3];
			OUTPUT_CLONE_DIR = args[4];

		}
		FileReader fileReader = new FileReader(INPUT_JSON_READ_FILE);
		JSONParser parser = new JSONParser();

		JSONObject jsonObject = (JSONObject) parser.parse(fileReader);
		JSONArray jsonArray = (JSONArray) jsonObject.get("items");

		if (noOfProjects == -1) {
			noOfProjects = jsonArray.size();
		}

		JSONObject innerObj = (JSONObject) jsonArray.get(0);

		System.out.println(innerObj.get("clone_url"));

		StringBuffer fileNames = new StringBuffer();
		StringBuffer repoNames = new StringBuffer();
		for (int i = 0; i < noOfProjects; i++) {
			JSONObject obj = (JSONObject) jsonArray.get(i);
			fileNames.append(obj.get("name") + "\n");
			
			StringBuffer sb =new StringBuffer((String)obj.get("issues_url"));
			sb.delete(sb.indexOf("{"), sb.indexOf("}")+1);
			
			repoNames.append(sb.toString() + "\n");
			cloneRepo((String) obj.get("clone_url"), (String) obj.get("name"));
			//getChangedFiles(getLastCommit(OUTPUT_CLONE_DIR+obj.get("name")),OUTPUT_CLONE_DIR+obj.get("name"));
		}

		Utility.writeMyFile(OUTPUT_PROJECT_NAMES, fileNames.toString());
		Utility.writeMyFile(OUTPUT_REPO_LINKS, repoNames.toString());
		}

	private static void cloneRepo(String repoUrl, String name) throws IOException, GitAPIException {

		// prepare a new folder for the cloned repository
		File localPath = new File(OUTPUT_CLONE_DIR + name);

		if (!localPath.exists()) {
			localPath.mkdirs();
		}
		
		// then clone
		System.out.println("Cloning from " + repoUrl + " to " + localPath);
		long start = System.currentTimeMillis();
		Git result=null;
		try {
			result= Git.cloneRepository().setURI(repoUrl).setDirectory(localPath).call();
			// Note: the call() returns an opened repository already which needs to be
			// closed to avoid file handle leaks!

		}
		finally {
			/*if(result!=null) {
				result.close();
			}*/
		}
		System.out.println("Clonning completed for repository. " + "\nTime Taken:" + (System.currentTimeMillis() - start) + " ms.");

	}
	
	/*private static void getChangedFiles(String commitId,String location) throws IOException, GitAPIException {
        try (Repository repository = openJGitCookbookRepository(location)) {
            try (Git git = new Git(repository)) {
                // to compare against the "previous" commit, you can use
                // the caret-notation
               
                listDiff(repository, git,
                		commitId+"^",
                		commitId);
            
            }
        }
    }

    private static void listDiff(Repository repository, Git git, String oldCommit, String newCommit) throws GitAPIException, IOException {
        final List<DiffEntry> diffs = git.diff()
                .setOldTree(prepareTreeParser(repository, oldCommit))
                .setNewTree(prepareTreeParser(repository, newCommit))
                .call();

        System.out.println("Found: " + diffs.size() + " differences");
        for (DiffEntry diff : diffs) {
            //System.out.println("Diff: " + diff.getChangeType() + ": " + (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
        	System.out.println("Diff: " +(diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath()));
        }
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            
            walk.dispose();
            return treeParser;
        }
    }*/
    
    /*private static Repository openJGitCookbookRepository(String location) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir(new File(location)) // scan up the file system tree
                .build();
    }*/
    
    /*private static String getLastCommit(String location) throws IOException, NoHeadException, GitAPIException {
    	try (Repository repository = openJGitCookbookRepository(location)) {
    		String entireCommit="";
            try (Git git = new Git(repository)) {
                Iterable<RevCommit> commits = git.log().all().call();
                for (RevCommit commit : commits) {
                	entireCommit=commit.name();
                    System.out.println("LogCommit: " + commit.name());
                    break;
                }
            }
            return entireCommit;
        }
    }*/
}
