package org.jocker.setup

import com.cloudbees.plugins.credentials.impl.*;
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.domains.*;

class CredentialsUtil implements Serializable {
    def getStore () {
        return Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()
    }
    def AddUsernameAndPassword(id, username, password) {
        Credentials usernameAndPasswordCredential = (Credentials) new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, id, "description", username, password)

        getStore().addCredentials( Domain.global(), usernameAndPasswordCredential)
        return this
    }
}
