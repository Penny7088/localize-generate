<#list list as localizable>
    <#if (localizable.comment > 0) >
    <!--${localizable.value} -->
    <#else>
    <!--${localizable.description} -->
    <string name="${localizable.key}">${localizable.value}</string>
    </#if>
</#list>