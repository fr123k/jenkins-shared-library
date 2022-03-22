package org.jocker.setup

import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration

class JenkinsLocation implements Serializable {
    JenkinsLocation() {}
    def configure(server) {
        // get Jenkins location configuration
        def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()

        // set Jenkins URL
        jenkinsLocationConfiguration.setUrl(server)

        // save current Jenkins state to disk
        jenkinsLocationConfiguration.save()
        return this
    }
}
