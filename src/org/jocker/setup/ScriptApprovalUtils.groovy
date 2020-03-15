package org.jocker.setup

import static org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval.get
import jenkins.model.Jenkins

class ScriptApprovalUtils implements Serializable {
    def defaultApprovals = [
        //used in csrf.groovy
        'staticMethod jenkins.model.Jenkins getInstance',
        'method jenkins.model.Jenkins setCrumbIssuer hudson.security.csrf.CrumbIssuer',
        //used in cascPlugin.groovy
        'method jenkins.model.Jenkins getExtensionList java.lang.Class',
        'method io.jenkins.plugins.casc.ConfigurationAsCode configure',
        //used in userPublicKeys.groovy
        'staticMethod hudson.model.User get java.lang.String',
        'staticMethod java.lang.System getenv',
        'new org.jenkinsci.main.modules.cli.auth.ssh.UserPropertyImpl java.lang.String',
        'method hudson.model.User addProperty hudson.model.UserProperty',
        'method hudson.model.Saveable save',
        'new java.io.File java.lang.String',
        'staticMethod org.codehaus.groovy.runtime.DefaultGroovyMethods getText java.io.File',
        //used in timezone.groovy
        'staticMethod org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval get',
        'staticMethod java.lang.System setProperty java.lang.String java.lang.String',
    ]

    def defaultScriptApprovals = [
        //the pipeline script in the jobDSL/pulumi.groovy
        '6d2ccc5267db0f3b500aa96a1ec53264613a1209'
    ]
    
    def approvals(signatures) {
        signatures.each {
            approve it
        }
    }

    def approvalScripts(signatures) {
        signatures.each {
            approveScript it
        }
    }

    def approve(signature) { get().approveSignature(signature) }
    def approveScript(hash) { get().approveScript(hash) }

    def approveDefaults() {
        approvals(defaultApprovals)
        return this
    }

    def approveDefaultScripts() {
        approvalScripts(defaultScriptApprovals)
        return this
    }
}
