def call() {
    node {
        stage('Checkout') {
            cleanWs()
            sh """
            git clone https://github.com/fr123k/jocker.git work --depth 1
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
                targets: 'work/shared-library/jobDSL/folders.groovy', 
                sandbox: false, 
                removedJobAction: 'IGNORE',
                lookupStrategy: 'JENKINS_ROOT'
            )
        }
    }
}
