package org.jocker.setup

import jenkins.model.Jenkins

class SetupWizard implements Serializable {
    def config
    def cascPlugin
    def scriptApproval
    SetupWizard(config) {this.config = config}
    def setup() {
        scriptApproval  = new ScriptApprovalUtils()
                            .approveDefaultScripts()
        cascPlugin      = new CascPlugin(config, '/var/jenkins_home/casc-config/')
                            .configure()

        //disabling of csrf
        Jenkins.getInstance().setCrumbIssuer(null)
        println("Disable CSRF protection")

        //timezone
        System.setProperty('org.apache.commons.jelly.tags.fmt.timeZone', 'Europe/Berlin')

        //user public key
        def user                    = hudson.model.User.get('admin')
        def jenkinsCliPubKeyContent = new File('/var/jenkins_home/.ssh/jenkins-cli.pub').text
        def pubKey                  = new org.jenkinsci.main.modules.cli.auth.ssh.UserPropertyImpl(jenkinsCliPubKeyContent)
        user.addProperty(pubKey)
        user.save()

        return this
    }

    def getCascPlugin() { return cascPlugin }
    def getCcriptApproval() { return scriptApproval }

    def getConfig() { return config }

}
