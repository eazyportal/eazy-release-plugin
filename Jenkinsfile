@Library("eazyjenkins-library") _

EazyLibraryPipeline(
    projectName: 'eazyrelease-plugin',
    stages: [
        Preparation: { pipelineConfig ->
            _preparationStage(pipelineConfig) {
                prepareRepositoryForRelease()

                setReleaseVersion()

                gradleExec('lockDependencyVersion', '--write-locks')

                finalizeReleaseVersion()
            }
        },
        Build: { _buildStage() },
        Quality: { _qualityStage() },
        Release: { pipelineConfig ->
        },
        post: { _postStage() }
    ]
)
