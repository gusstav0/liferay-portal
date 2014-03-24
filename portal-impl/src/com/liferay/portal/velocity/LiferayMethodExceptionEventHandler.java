/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.velocity;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.app.event.MethodExceptionEventHandler;

/**
 * @author Raymond Augé
 * @author Peter Shin
 */
public class LiferayMethodExceptionEventHandler
	implements MethodExceptionEventHandler {

	@Override
	public Object methodException(
			@SuppressWarnings("rawtypes") Class clazz, String method,
			Exception e)
		throws Exception {

		StringBundler sb = new StringBundler(9);

		sb.append("Unable to execute method ");
		sb.append(method);
		sb.append(StringPool.SPACE);
		sb.append(StringPool.OPEN_CURLY_BRACE);
		sb.append("exception=");
		sb.append(e);
		sb.append(StringPool.COMMA_AND_SPACE);
		sb.append(getClassMessage(clazz));
		sb.append(StringPool.CLOSE_CURLY_BRACE);

		_log.error(sb.toString(), e);

		return null;
	}

	protected String getClassMessage(Class<?> clazz) {
		if (clazz == null) {
			return "class=null";
		}

		if (!ProxyUtil.isProxyClass(clazz)) {
			return "className=".concat(clazz.getName());
		}

		Class<?>[] interfaceClasses = clazz.getInterfaces();

		if (interfaceClasses == null) {
			return "className=".concat(clazz.getName());
		}

		List<String> proxyInterfaceClassNames = new ArrayList<String>();

		for (Class<?> interfaceClass : interfaceClasses) {
			proxyInterfaceClassNames.add(interfaceClass.getName());
		}

		return "proxyInterfaceClassNames=".concat(
			StringUtil.merge(
				proxyInterfaceClassNames, StringPool.COMMA_AND_SPACE));
	}

	private static Log _log = LogFactoryUtil.getLog(
		LiferayMethodExceptionEventHandler.class);

}