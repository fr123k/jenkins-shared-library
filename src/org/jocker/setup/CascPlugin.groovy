package org.jocker.setup

import jenkins.model.Jenkins

class CascPlugin implements Serializable {
    def src, target
    CascPlugin(src, target) {
        this.src = src
        this.target = target
    }
    def configure() {
        def proc = "rsync -r ${src} ${target}".execute()
        println proc.in.text
        println proc.err.text

        // trigger configuration
        System.setProperty('casc.jenkins.config', target)
        def jcacPlugin = Jenkins.instance.getExtensionList(io.jenkins.plugins.casc.ConfigurationAsCode.class).first()
        jcacPlugin.configure()
        return this
    }
}
