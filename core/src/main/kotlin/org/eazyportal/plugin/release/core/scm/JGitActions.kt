package org.eazyportal.plugin.release.core.scm

import org.eazyportal.plugin.release.core.scm.exception.ScmActionException
import org.eazyportal.plugin.release.core.version.model.Version
import org.eclipse.jgit.api.AddCommand
import org.eclipse.jgit.api.CreateBranchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeCommand.FastForwardMode
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.SubmoduleConfig.FetchRecurseSubmodulesMode
import org.eclipse.jgit.merge.ContentMergeStrategy
import org.eclipse.jgit.transport.TagOpt
import org.slf4j.LoggerFactory
import java.io.File

class JGitActions : ScmActions {

    override fun add(
        workingDir: File,
        vararg filePaths: String
    ) {
        openGit(workingDir) {
            val addCommand: AddCommand = add()

            filePaths.forEach {
                addCommand.addFilepattern(it)
            }

            addCommand.call()
        }
    }

    override fun checkout(
        workingDir: File,
        toRef: String
    ) {
        openGit(workingDir) {
            if (repository.findRef(toRef) != null) {
                checkout()
                    .setName(toRef)
                    .call()
            } else {
                val remote = repository.remoteNames
                    .first { repository.findRef("$it/$toRef") != null }

                checkout()
                    .setName(toRef)
                    .setCreateBranch(true)
                    .setStartPoint("refs/remotes/$remote/$toRef")
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                    .call()
            }
        }
    }

    override fun commit(
        workingDir: File,
        message: String
    ) {
        openGit(workingDir) {
            commit()
                .setMessage(message)
                .call()
        }
    }

    override fun fetch(
        workingDir: File,
        remote: String
    ) {
        openGit(workingDir) {
            fetch()
                .setRemote(remote)
                .setRemoveDeletedRefs(true)
                .setTagOpt(TagOpt.FETCH_TAGS)
                .setRecurseSubmodules(FetchRecurseSubmodulesMode.YES)
                .call()
        }
    }

    override fun getCommits(
        workingDir: File,
        fromRef: String?,
        toRef: String
    ): List<String> =
        openGit(workingDir) {
            val resolvedFromRef = repository.findRef(fromRef)
            val resolvedToRef = repository.findRef(toRef)

            val logCommand = log()

            if ((resolvedFromRef != null) && (resolvedToRef != null)) {
                logCommand.addRange(resolvedFromRef.objectId, resolvedToRef.objectId)
            } else if (resolvedFromRef != null) {
                logCommand.add(resolvedFromRef.objectId)
            }

            logCommand.call()
                .map { it.fullMessage }
        }

    override fun getLastTag(
        workingDir: File,
        fromRef: String
    ): String =
        openGit(workingDir) {
            val resolvedFromRef = repository.findRef(fromRef)

            describe()
                .setAbbrev(0)
                .setTags(true)
                .setTarget(resolvedFromRef?.objectId)
                .call()
                ?: throw ScmActionException(null)
        }

    override fun getSubmodules(workingDir: File): List<String> {
        LOGGER.warn("Not yet implemented")

        return emptyList()
    }

    override fun getTags(
        workingDir: File,
        fromRef: String
    ): List<String> =
        openGit(workingDir) {
            val resolvedFromRef = repository.findRef(fromRef)

            tagList()
                .setContains(resolvedFromRef?.objectId)
                .call()
                .map { it.name }
                .map { it.substring(it.lastIndexOf('/') + 1) }
        }

    override fun mergeNoCommit(workingDir: File, fromBranch: String) {
        openGit(workingDir) {
            val fromBranchRef: Ref = repository.findRef(fromBranch)

            merge()
                .include(fromBranchRef)
                .setFastForward(FastForwardMode.NO_FF)
                .setCommit(false)
                .setContentMergeStrategy(ContentMergeStrategy.THEIRS)
                .call()
        }
    }

    override fun push(
        workingDir: File,
        remote: String,
        vararg branches: String
    ) {
        openGit(workingDir) {
            val pushCommand = push()
                .setAtomic(true)
                .setPushTags()
                .setRemote(remote)

            branches.forEach {
                pushCommand.add("$it:$it")
            }

            pushCommand.call()
        }
    }

    override fun tag(
        workingDir: File,
        version: Version
    ) {
        openGit(workingDir) {
            tag()
                .setName(version.toString())
                .call()
        }
    }

    private fun <T> openGit(workingDir: File, block: Git.() -> T): T {
        try {
            Git.open(workingDir).use {
                return it.block()
            }
        } catch (exception: ScmActionException) {
            throw exception
        } catch (exception: Exception) {
            throw ScmActionException(exception)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JGitActions::class.java)
    }

}
