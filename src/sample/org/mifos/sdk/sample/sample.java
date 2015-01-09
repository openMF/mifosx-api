/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifos.sdk.sample;

import org.mifos.sdk.MifosXClient;
import org.mifos.sdk.MifosXClientFactory;
import org.mifos.sdk.MifosXConnectException;
import org.mifos.sdk.MifosXProperties;
import org.mifos.sdk.MifosXResourceException;
import org.mifos.sdk.client.ClientService;
import org.mifos.sdk.client.domain.Client;
import org.mifos.sdk.client.domain.commands.AssignUnassignStaffCommand;
import org.mifos.sdk.group.GroupService;
import org.mifos.sdk.group.domain.Group;
import org.mifos.sdk.office.OfficeService;
import org.mifos.sdk.office.domain.Office;
import org.mifos.sdk.staff.StaffService;
import org.mifos.sdk.staff.domain.Staff;

import java.io.IOException;

/**
 * Sample to showcase the usage of the SDK.
 */
public class Sample {

    public static void main(String[] args) throws IOException {
        final MifosXProperties properties = MifosXProperties
            .url("https://demo.openmf.org/mifosng-provider/api/v1")
            .tenant("default")
            .username("mifos")
            .password("password")
            .build();

        final MifosXClient mifosClient = MifosXClientFactory.get(properties);

        try {
            mifosClient.login();

            final OfficeService officeService = mifosClient.officeService();
            final Office office = officeService.findOffice(1L);
            System.out.println("Fetched office : " + office.getName());

            final ClientService clientService = mifosClient.clientService();
            final Client client = clientService.findClient(1L);
            System.out.println("Fetched client : " + client.getDisplayName());

            final StaffService staffService = mifosClient.staffService();
            final Staff staff = staffService.findStaff(1L);
            System.out.println("Fetched staff : " + staff.getDisplayName());

            final AssignUnassignStaffCommand command = AssignUnassignStaffCommand
                .staffId(staff.getResourceId()).build();
            clientService.assignStaff(client.getClientId(), command);
            System.out.println("Assigned staff to client");

            final GroupService groupService = mifosClient.groupService();
            final Group group = groupService.findGroup(1L, null);
            System.out.println("Fetched group : " + group.getName());

            final org.mifos.sdk.group.domain.commands.AssignUnassignStaffCommand command1 =
                org.mifos.sdk.group.domain.commands.AssignUnassignStaffCommand
                    .staffId(1L).build();
            groupService.assignStaff(1L, command1);
            System.out.println("Assigned staff to group");
        } catch (MifosXConnectException mcex) {
            mcex.printStackTrace();
        } catch (MifosXResourceException mrex) {
            mrex.printStackTrace();
        }

        mifosClient.logout();
    }

}
