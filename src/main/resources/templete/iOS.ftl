<#list list as localizable>
<#if (localizable.comment > 0) >
//${localizable.value}
<#else>
/* ${localizable.description} */
"${localizable.key}"="${localizable.value}";
</#if>
</#list>