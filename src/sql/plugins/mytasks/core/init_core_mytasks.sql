--
-- Dumping data for table core_portlet_type
--
INSERT INTO core_portlet_type (id_portlet_type,name,url_creation,url_update,home_class,plugin_name,url_docreate,create_script,create_specific,create_specific_form,url_domodify,modify_script,modify_specific,modify_specific_form) VALUES 
('MYTASKS_PORTLET','mytasks.portlet.name','plugins/mytasks/CreatePortletMyTasks.jsp','plugins/mytasks/ModifyPortletMyTasks.jsp','fr.paris.lutece.plugins.mytasks.business.portlet.MyTasksPortletHome','mytasks','plugins/mytasks/DoCreatePortletMyTasks.jsp','/admin/portlet/script_create_portlet.html','','','plugins/mytasks/DoModifyPortletMyTasks.jsp','/admin/portlet/script_modify_portlet.html','','');

--
-- Dumping data for table core_style
--
INSERT INTO core_style (id_style,description_style,id_portlet_type,id_portal_component) VALUES (802,'Défaut','MYTASKS_PORTLET',0);

--
-- Dumping data for table core_style_mode_stylesheet
--
INSERT INTO core_style_mode_stylesheet (id_style,id_mode,id_stylesheet) VALUES (802,0,309);

--
-- Dumping data for table core_stylesheet
--
INSERT INTO core_stylesheet (id_stylesheet, description, file_name, source) VALUES (309,'Rubrique Gestion des tâches simple - Défaut','portlet_mytasks.xsl','<?xml version=\"1.0\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n\r\n	<xsl:param name=\"site-path\" select=\"site-path\" />\r\n	<xsl:variable name=\"portlet-id\" select=\"portlet/portlet-id\" />\r\n	\r\n	<xsl:template match=\"portlet\">\r\n	\r\n		\r\n	<xsl:variable name=\"device_class\">\r\n	<xsl:choose>\r\n		<xsl:when test=\"string(display-on-small-device)=\'0\'\">hide-for-small</xsl:when>\r\n		<xsl:otherwise></xsl:otherwise>\r\n	</xsl:choose>\r\n	</xsl:variable>\r\n	\r\n		<div class=\"portlet  {$device_class} append-bottom -lutece-border-radius\" id=\"portlet_id_{portlet-id}\">\r\n	        <xsl:if test=\"not(string(display-portlet-title)=\'1\')\">\r\n				<h3 class=\"portlet-header\">\r\n					<xsl:value-of disable-output-escaping=\"yes\" select=\"portlet-name\" />\r\n				</h3>\r\n				<br />\r\n	        </xsl:if>\r\n	\r\n			<div id=\"mytasks\" class=\"portlet-content\">\r\n				<xsl:apply-templates select=\"mytasks-list\" />\r\n			</div>\r\n		</div>\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"mytasks-list\">\r\n		<p>\r\n			<a href=\"jsp/site/Portal.jsp?page=mytasks&amp;action=add_mytask&amp;mytasks_url_return={mytasks-url-return}\">\r\n				<span class=\"mytasks-plus\">+</span>\r\n				<xsl:text disable-output-escaping=\"yes\">&#160;Ajouter une t&#226;che</xsl:text>\r\n			</a>\r\n		</p>\r\n		<form method=\"post\" action=\"jsp/site/plugins/mytasks/DoActionMyTask.jsp\">\r\n			<input type=\"hidden\" name=\"action\" value=\"do_change_mytasks_status\" />\r\n			<input type=\"hidden\" name=\"mytasks_url_return\" value=\"{mytasks-url-return}\" />\r\n	   		<xsl:apply-templates select=\"mytask\" />\r\n	   		<input type=\"submit\" value=\"Rafra&#238;chir\" />\r\n	   		<a href=\"jsp/site/plugins/mytasks/DoActionMyTask.jsp?action=do_delete_completed_mytasks&amp;mytasks_url_return={mytasks-url-return}\" class=\"mytasks-button\">\r\n	    		Supprimer les t&#226;ches termin&#233;es\r\n	    	</a>\r\n	   	</form>\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"mytask\">\r\n		<xsl:variable name=\"url-return\" select=\"parent::node()/mytasks-url-return\" />\r\n		<xsl:choose>\r\n			<xsl:when test=\"string(mytask-is-done) = \'1\'\">\r\n				<p>\r\n					<input type=\"checkbox\" name=\"id_mytask\" value=\"{id-mytask}\" checked=\"checked\" />\r\n					<span class=\"mytasks-done\">\r\n						<a href=\"jsp/site/Portal.jsp?page=mytasks&amp;action=update_mytask&amp;id_mytask={id-mytask}&amp;mytasks_url_return={$url-return}\">\r\n							<xsl:value-of disable-output-escaping=\"yes\" select=\"mytask-name\" />\r\n						</a>\r\n						<br/>\r\n						<xsl:value-of disable-output-escaping=\"yes\" select=\"mytask-date\" />\r\n					</span>\r\n				</p>\r\n			</xsl:when>\r\n			<xsl:otherwise>\r\n				<p>\r\n					<input type=\"checkbox\" name=\"id_mytask\" value=\"{id-mytask}\" />\r\n					<span>\r\n						<a href=\"jsp/site/Portal.jsp?page=mytasks&amp;action=update_mytask&amp;id_mytask={id-mytask}&amp;mytasks_url_return={$url-return}\">\r\n							<xsl:value-of disable-output-escaping=\"yes\" select=\"mytask-name\" />\r\n						</a>\r\n						<br/>\r\n						<xsl:value-of disable-output-escaping=\"yes\" select=\"mytask-date\" />\r\n					</span>\r\n				</p>\r\n			</xsl:otherwise>\r\n		</xsl:choose>\r\n    </xsl:template>\r\n\r\n</xsl:stylesheet>\r\n');

--
-- Dumping data for table core_admin_role
--
INSERT INTO core_admin_role (role_key,role_description) VALUES ('mytasks_manager','MyTasks management');

--
-- Dumping data for table core_admin_role_resource
--
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES (198,'mytasks_manager','MYTASKS','*','*');

--
-- Dumping data for table core_user_role
--
INSERT INTO core_user_role (role_key,id_user) VALUES ('mytasks_manager',1);
INSERT INTO core_user_role (role_key,id_user) VALUES ('mytasks_manager',2);

--
-- Init  table core_admin_dashboard
--
INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('myTasksAdminDashboardComponent', 1, 1);
