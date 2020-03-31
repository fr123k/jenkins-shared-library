def call(arguments) {
    node {
        stage('Checkout') {
            // TODO fix the git clone checkout mess to just clone a specific branch
            cleanWs()
            // git clone --branch ${arguments.revision} https://github.com/fr123k/jocker.git work --depth 1
            sh """
            git clone https://github.com/fr123k/jocker.git work
            cd work
            git checkout ${arguments.revision}
            cd ..
            """
        }
        stage("Create jobs") {
            sh """
            ls -lha work
            """
            jobDsl(
                targets: 'work/shared-library/jobDSL/folders.groovy',
                sandbox: false,
                removedJobAction: 'IGNORE',
                lookupStrategy: 'JENKINS_ROOT'
            )
            jobDsl(
                targets: 'work/shared-library/jobDSL/*.groovy',
                sandbox: false,
                removedJobAction: 'DELETE',
                lookupStrategy: 'JENKINS_ROOT'
            )
        }
    }
}
