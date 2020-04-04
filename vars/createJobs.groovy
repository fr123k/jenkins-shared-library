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
        }
    }
}
