package org.jetbrains.cabal.psi

import com.intellij.lang.ASTNode
import org.jetbrains.cabal.psi.SingleValueField
import org.jetbrains.cabal.psi.PathsField
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

public class DataDirField(node: ASTNode) : SingleValueField(node), PathsField {

    public override fun isValidFile(file: VirtualFile): Boolean = file.isDirectory()

    public override fun getParentDirs(prefixPath: Path, originalRootDir: VirtualFile): List<VirtualFile> {
        if (prefixPath.isAbsolute()) return listOf()
        return super<PathsField>.getParentDirs(prefixPath, originalRootDir)
    }
}
