package io.github.taikonaut3.virtue.config.manager;

import io.github.taikonaut3.virtue.config.ClientCaller;
import io.github.taikonaut3.virtue.config.MatchRule;
import io.github.taikonaut3.virtue.config.ServerCaller;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractRuleManager<T> extends AbstractManager<T> {

    protected final Map<T, RuleWrapper<T>> ruleMap = new HashMap<>();

    protected AbstractRuleManager(Virtue virtue) {
        super(virtue);
    }

    public void addProtocolRule(T config, MatchRule.Scope scope, String... rules) {
        RuleWrapper<T> ruleWrapper = ruleMap.computeIfAbsent(config, RuleWrapper<T>::new);
        if (scope == MatchRule.Scope.all) {
            ruleWrapper.addProtocolRulesForClient(rules);
            ruleWrapper.addProtocolRulesForServer(rules);
        } else if (scope == MatchRule.Scope.client) {
            ruleWrapper.addProtocolRulesForClient(rules);
        } else if (scope == MatchRule.Scope.server) {
            ruleWrapper.addProtocolRulesForServer(rules);
        }
    }

    public void addPathRule(T config, MatchRule.Scope scope, String... rules) {
        RuleWrapper<T> ruleWrapper = ruleMap.computeIfAbsent(config, RuleWrapper<T>::new);
        if (scope == MatchRule.Scope.all) {
            ruleWrapper.addPathRulesForClient(rules);
            ruleWrapper.addPathRulesForServer(rules);
        } else if (scope == MatchRule.Scope.client) {
            ruleWrapper.addPathRulesForClient(rules);
        } else if (scope == MatchRule.Scope.server) {
            ruleWrapper.addPathRulesForServer(rules);
        }
    }

    public void executeRules() {
        List<ServerCaller<?>> serverCallers = virtue.configManager().remoteServiceManager().serverCallers();
        List<ClientCaller<?>> clientCallers = virtue.configManager().remoteCallerManager().clientCallers();
        for (RuleWrapper<T> ruleWrapper : ruleMap.values()) {
            List<ServerCaller<?>> matchedServerCallers = matchedServerCallers(ruleWrapper.serverRegexWrapper(), serverCallers);
            List<ClientCaller<?>> matchedClientCallers = matchedClientCallers(ruleWrapper.clientRegexWrapper(), clientCallers);
            doExecuteRules(ruleWrapper.config, matchedServerCallers, matchedClientCallers);
        }
    }

    protected abstract void doExecuteRules(T config, List<ServerCaller<?>> matchedServerCallers, List<ClientCaller<?>> matchedClientCallers);

    private List<ServerCaller<?>> matchedServerCallers(RuleWrapper.RegexWrapper serverRegexWrapper, List<ServerCaller<?>> serverCallers) {
        List<String> protocolRules = serverRegexWrapper.protocolRules;
        List<String> pathRules = serverRegexWrapper.pathRules;
        return serverCallers.stream().filter(serverCaller -> {
            for (String protocolRule : protocolRules) {
                if (!isMatch(protocolRule, serverCaller.protocol())) {
                    return false;
                }
            }
            for (String pathRule : pathRules) {
                if (!isMatch(pathRule, serverCaller.path())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    private List<ClientCaller<?>> matchedClientCallers(RuleWrapper.RegexWrapper clientRegexWrapper, List<ClientCaller<?>> clientCallers) {
        List<String> protocolRules = clientRegexWrapper.protocolRules;
        List<String> pathRules = clientRegexWrapper.pathRules;
        return clientCallers.stream().filter(serverCaller -> {
            for (String protocolRule : protocolRules) {
                if (!isMatch(protocolRule, serverCaller.protocol())) {
                    return false;
                }
            }
            for (String pathRule : pathRules) {
                if (!isMatch(pathRule, serverCaller.path())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    private boolean isMatch(String regex, String content) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content).find();
    }

    protected static class RuleWrapper<T> {

        private final T config;

        private volatile RegexWrapper clientRegexWrapper;

        private volatile RegexWrapper serverRegexWrapper;

        public RuleWrapper(T config) {
            this.config = config;
        }

        public void addProtocolRulesForClient(String... rules) {
            RegexWrapper regexWrapper = clientRegexWrapper();
            regexWrapper.addProtocolRules(rules);
        }

        public void addPathRulesForClient(String... rules) {
            RegexWrapper regexWrapper = clientRegexWrapper();
            regexWrapper.addPathRules(rules);
        }

        public void addProtocolRulesForServer(String... rules) {
            RegexWrapper regexWrapper = serverRegexWrapper();
            regexWrapper.addProtocolRules(rules);
        }

        public void addPathRulesForServer(String... rules) {
            RegexWrapper regexWrapper = serverRegexWrapper();
            regexWrapper.addPathRules(rules);
        }

        public List<String> clientProtocolRules() {
            return clientRegexWrapper.protocolRules;
        }

        public List<String> clientPathRules() {
            return clientRegexWrapper.pathRules;
        }

        public List<String> serverProtocolRules() {
            return serverRegexWrapper.protocolRules;
        }

        public List<String> serverPathRules() {
            return serverRegexWrapper.pathRules;
        }

        private RegexWrapper clientRegexWrapper() {
            if (clientRegexWrapper == null) {
                synchronized (this) {
                    if (clientRegexWrapper == null) {
                        clientRegexWrapper = new RegexWrapper();
                    }
                }
            }
            return clientRegexWrapper;
        }

        private RegexWrapper serverRegexWrapper() {
            if (serverRegexWrapper == null) {
                synchronized (this) {
                    if (serverRegexWrapper == null) {
                        serverRegexWrapper = new RegexWrapper();
                    }
                }
            }
            return serverRegexWrapper;
        }

        static class RegexWrapper {

            final List<String> protocolRules = new LinkedList<>();

            final List<String> pathRules = new LinkedList<>();

            public void addProtocolRules(String... rules) {
                if (rules != null) {
                    Arrays.stream(rules).filter(Objects::nonNull).forEach(protocolRules::add);
                }
            }

            public void addPathRules(String... rules) {
                if (rules != null) {
                    Arrays.stream(rules).filter(Objects::nonNull).forEach(pathRules::add);
                }
            }

        }
    }
}
