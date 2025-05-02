package com.example.test.dbconfig;


import com.example.test.dbconfig.member_billing.MemberBillingDataSource;
import com.example.test.dbconfig.oac_member.OACMemberDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;

@Slf4j
public class HibernateArchiveInterceptor implements StatementInspector {

        @Override
        public String inspect(String sql) {
                if (sql == null) {
                        return null;
                }
                sql = sql.replace(MemberBillingDataSource.member_billing.name(), MemberBillingDataSource.member_billing_archive.name());
                sql = sql.replace(OACMemberDataSource.oac_member.name(), OACMemberDataSource.oac_member_archive.name());
                return sql;
        }
}
