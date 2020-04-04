@NonCPS
def installPlugins(String plugins) {
    if (!plugins?.trim())
        return
    installedPlugins = jenkins.model.Jenkins.instance.getPluginManager().getPlugins().collect { it.getShortName() }

    plugins.eachLine { line ->
        if (line?.trim() && !line.startsWith( '#' )) {
            foundPlugin = installedPlugins.findAll { key -> "${key}" == "${line}" }
            echo "Check if plugin ${line} is installed"
            if( !foundPlugin ){
                echo "Install plugin ${line}"
                Jenkins.instance.updateCenter.getPlugin(line).deploy(true)
            } else {
                echo "Skip plugin already installed ${line}"
            }
        }
    }
}

def call(String repository="https://github.com/fr123k/jocker.git", String revision="master", String jobDSLPath="jenkins/jobDSL") {
    node {
        stage('Checkout') {
            // TODO fix the git clone checkout mess to just clone a specific branch
            cleanWs()
            // git clone --branch ${arguments.revision} https://github.com/fr123k/jocker.git work --depth 1
            sh """
            git clone ${repository} work
            cd work
            git checkout ${revision}
            cd ..
            """
        }
        stage("Create jobs") {
            sh """
            ls -lha work
            """
            // https://issues.jenkins-ci.org/browse/JENKINS-44142
            // --> Note: when using multiple Job DSL build steps in a single job, set this to "Delete" only for the last Job DSL build step. 
            // Otherwise views may be deleted and re-created. See JENKINS-44142 for details.
            jobDsl(
                targets: "work/${jobDSLPath}/folders.groovy",
                sandbox: false,
                removedJobAction: 'IGNORE',
                lookupStrategy: 'JENKINS_ROOT'
            )
            jobDsl(
                targets: "work/${jobDSLPath}/*.groovy",
                sandbox: false,
                removedJobAction: 'DELETE',
                lookupStrategy: 'JENKINS_ROOT',
            )
            if (fileExists('work/plugins.txt')) {
                echo "check plugins.txt"
                plugins = readFile "work/plugins.txt"
                installPlugins(plugins)
            }
        }
    }
}
