<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/fruits">
        Fruits:
        <xsl:for-each select="fruit">
            - <xsl:value-of select="name"/>: <xsl:value-of select="description"/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>