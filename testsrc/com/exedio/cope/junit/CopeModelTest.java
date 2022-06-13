/*
 * Copyright (C) 2004-2015  exedio GmbH (www.exedio.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.exedio.cope.junit;

import static java.util.Objects.requireNonNull;

import com.exedio.cope.ConnectProperties;
import com.exedio.cope.Model;
import com.exedio.cope.util.Properties;
import java.util.Collection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * An abstract test case class for tests creating/using some persistent data.
 * @author Ralf Wiebicke
 */
@SuppressWarnings("AbstractClassWithoutAbstractMethods") // OK: has default implementations for all overridable methods
public abstract class CopeModelTest
{
	protected final Model model;

	protected CopeModelTest(final Model model)
	{
		this.model = requireNonNull(model, "model");
	}

	public final Model getModel()
	{
		return model;
	}

	private static ConnectProperties getConnectProperties()
	{
		return ConnectProperties.create(new Properties.Source()
		{
			@Override
			public Collection<String> keySet()
			{
				return null;
			}

			@Override
			public String getDescription()
			{
				return getClass().toString();
			}

			@Override
			public String get(final String key)
			{
				if("connection.url".equals(key))
					return "jdbc:hsqldb:mem:copeim4javatest";
				else if("connection.username".equals(key))
					return "sa";
				else if("connection.password".equals(key))
					return "";
				else
					return null;
			}
		});
	}

	/**
	 * Override this method returning false if you do not want
	 * method {@link #setUpCopeModelTest()} to create a transaction for you.
	 * The default implementation returns true.
	 */
	protected boolean doesManageTransactions()
	{
		return true;
	}

	protected String getTransactionName()
	{
		return "tx:" + getClass().getName();
	}

	@BeforeEach
	private void setUpCopeModelTest()
	{
		ModelConnector.connectAndCreate(model, getConnectProperties());
		model.deleteSchemaForTest(); // typically faster than checkEmptySchema

		if(doesManageTransactions())
			model.startTransaction(getTransactionName());
	}

	@AfterEach
	private void tearDownCopeModelTest()
	{
		// NOTE:
		// do rollback even if manageTransactions is false
		// because test could have started a transaction
		model.rollbackIfNotCommitted();

		if(model.isConnected())
		{
			model.getConnectProperties().mediaFingerprintOffset().reset();
			model.setDatabaseListener(null);
		}
		model.removeAllChangeListeners();
		ModelConnector.dropAndDisconnect();
	}
}
