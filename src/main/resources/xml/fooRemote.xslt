<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/foo">
        <xsl:value-of select="bar"/>
    </xsl:template>
    <xsl:include href="https://raw.githubusercontent.com/DefensePoint/defensepoint-xxebenchmark/master/src/main/resources/xml/external.xslt"/>
</xsl:stylesheet>