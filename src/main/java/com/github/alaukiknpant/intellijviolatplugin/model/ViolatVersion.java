package com.github.alaukiknpant.intellijviolatplugin.model;

import java.io.Serializable;
import java.util.Objects;

public class ViolatVersion implements Comparable<ViolatVersion>, Serializable {
    private int major;
    private int minor;
    private int patch;

    public ViolatVersion() {}

    public ViolatVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public boolean isValid() {
        return this.major > 0 || this.minor > 0 || this.patch > 0;
    }

    public String toString() {
        return "v" + major + "." + minor + "." + patch;
    }

    @Override
    public int compareTo(ViolatVersion iv) {
        if (this.major != iv.getMajor()) {
            return Integer.compare(this.major,iv.getMajor());
        }
        if (this.minor != iv.getMinor()) {
            return Integer.compare(this.minor, iv.getMinor());
        }
        if (this.patch != iv.getPatch()) {
            return Integer.compare(this.patch, iv.getPatch());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViolatVersion that = (ViolatVersion) o;
        return major == that.major &&
                minor == that.minor &&
                patch == that.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    public int getMajor() {
        return major;
    }
    public int getMinor() {
        return minor;
    }
    public int getPatch() {
        return patch;
    }


}
