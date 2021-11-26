package org.eazyportal.plugin.release.core.version.model

import kotlin.math.min

internal class VersionComparator : Comparator<Version?> {

    override fun compare(left: Version?, right: Version?): Int {
        if ((left == null) && (right == null)) {
            return 0
        }
        if (left == null) {
            return 1
        }
        else if (right == null) {
            return -1
        }

        left.major.compareTo(right.major)
            .let {
                if (it != 0) {
                    return it
                }
            }

        left.minor.compareTo(right.minor)
            .let {
                if (it != 0) {
                    return it
                }
            }

        left.patch.compareTo(right.patch)
            .let {
                if (it != 0) {
                    return it
                }
            }

        // https://semver.org/#spec-item-11
        comparePreRelease(left.preRelease, right.preRelease).let {
            if (it != 0) {
                return it
            }
        }

        // build metadata is ignored from the comparison: https://semver.org/#spec-item-10
        return 0
    }

    private fun castPreReleasePart(part: String): Any {
        return part.let {
            try {
                it.toInt()
            }
            catch (exception: NumberFormatException) {
                it
            }
        }
    }

    private fun comparePreRelease(leftPreRelease: String?, rightPreRelease: String?): Int {
        if ((leftPreRelease == null) && (rightPreRelease == null)) {
            return 0
        }
        else if (leftPreRelease == null) {
            return -1
        }
        else if (rightPreRelease == null) {
            return 1
        }

        val leftPreReleaseParts = leftPreRelease.split(".")
        val rightPreReleaseParts = rightPreRelease.split(".")

        val maxIndex = min(leftPreReleaseParts.size, rightPreReleaseParts.size)
        for (i: Int in 0 until maxIndex) {
//            comparePreReleaseParts(castPreReleasePart(leftPreReleaseParts[i]), castPreReleasePart(rightPreReleaseParts[i])).let {
            comparePreReleaseParts(leftPreReleaseParts[i], rightPreReleaseParts[i]).let {
                if (it != 0) {
                    return it
                }
            }
        }

        if (leftPreReleaseParts.size < rightPreReleaseParts.size) {
            return -1
        }
        else if (leftPreReleaseParts.size > rightPreReleaseParts.size) {
            return 1
        }

        return 0
    }

    private fun comparePreReleaseParts(leftPreReleasePart: String, rightPreReleasePart: String): Int {
        val leftCasted = castPreReleasePart(leftPreReleasePart)
        val rightCasted = castPreReleasePart(rightPreReleasePart)

        if (leftCasted is Int) {
            if (rightCasted is String) {
                return -1
            }

            leftCasted.compareTo(rightCasted as Int).let {
                if (it != 0) {
                    return it
                }
            }
        }
        else if (leftCasted is String) {
            if (rightCasted is Int) {
                return 1
            }

            leftCasted.compareTo(rightCasted as String).let {
                if (it != 0) {
                    return it
                }
            }
        }

        return 0
    }

}
