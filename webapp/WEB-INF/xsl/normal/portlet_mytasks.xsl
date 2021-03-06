<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="site-path" select="site-path" />
	<xsl:variable name="portlet-id" select="portlet/portlet-id" />
	
	<xsl:template match="portlet">
	
		
	<xsl:variable name="device_class">
	<xsl:choose>
		<xsl:when test="string(display-on-small-device)='0'">hidden-phone</xsl:when>
		<xsl:otherwise></xsl:otherwise>
	</xsl:choose>
	</xsl:variable>
	
		<div class="portlet  {$device_class} append-bottom -lutece-border-radius" id="portlet_id_{portlet-id}">
	        <xsl:if test="not(string(display-portlet-title)='1')">
				<h3 class="portlet-header">
					<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
				</h3>
				<br />
	        </xsl:if>
	
			<div id="mytasks" class="portlet-content">
				<xsl:apply-templates select="mytasks-list" />
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="mytasks-list">
		<p>
			<a href="jsp/site/Portal.jsp?page=mytasks&amp;action=add_mytask&amp;mytasks_url_return={mytasks-url-return}">
				<span class="mytasks-plus">+</span>
				<xsl:text disable-output-escaping="yes">&#160;Ajouter une t&#226;che</xsl:text>
			</a>
		</p>
		<form method="post" action="jsp/site/plugins/mytasks/DoActionMyTask.jsp">
			<input type="hidden" name="action" value="do_change_mytasks_status" />
			<input type="hidden" name="mytasks_url_return" value="{mytasks-url-return}" />
	   		<xsl:apply-templates select="mytask" />
	   		<input type="submit" value="Rafra&#238;chir" />
	   		<a href="jsp/site/plugins/mytasks/DoActionMyTask.jsp?action=do_delete_completed_mytasks&amp;mytasks_url_return={mytasks-url-return}" class="mytasks-button">
	    		Supprimer les t&#226;ches termin&#233;es
	    	</a>
	   	</form>
	</xsl:template>
	
	<xsl:template match="mytask">
		<xsl:variable name="url-return" select="parent::node()/mytasks-url-return" />
		<xsl:choose>
			<xsl:when test="string(mytask-is-done) = '1'">
				<p>
					<input type="checkbox" name="id_mytask" value="{id-mytask}" checked="checked" />
					<span class="mytasks-done">
						<a href="jsp/site/Portal.jsp?page=mytasks&amp;action=update_mytask&amp;id_mytask={id-mytask}&amp;mytasks_url_return={$url-return}">
							<xsl:value-of disable-output-escaping="yes" select="mytask-name" />
						</a>
						<br/>
						<xsl:value-of disable-output-escaping="yes" select="mytask-date" />
					</span>
				</p>
			</xsl:when>
			<xsl:otherwise>
				<p>
					<input type="checkbox" name="id_mytask" value="{id-mytask}" />
					<span>
						<a href="jsp/site/Portal.jsp?page=mytasks&amp;action=update_mytask&amp;id_mytask={id-mytask}&amp;mytasks_url_return={$url-return}">
							<xsl:value-of disable-output-escaping="yes" select="mytask-name" />
						</a>
						<br/>
						<xsl:value-of disable-output-escaping="yes" select="mytask-date" />
					</span>
				</p>
			</xsl:otherwise>
		</xsl:choose>
    </xsl:template>

</xsl:stylesheet>
