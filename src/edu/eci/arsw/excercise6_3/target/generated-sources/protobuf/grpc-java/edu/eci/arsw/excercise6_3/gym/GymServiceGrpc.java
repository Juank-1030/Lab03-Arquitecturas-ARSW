package edu.eci.arsw.excercise6_3.gym;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: gym.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GymServiceGrpc {

  private GymServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "GymService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.SessionRequest,
      edu.eci.arsw.excercise6_3.gym.SessionResponse> getReserveSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReserveSession",
      requestType = edu.eci.arsw.excercise6_3.gym.SessionRequest.class,
      responseType = edu.eci.arsw.excercise6_3.gym.SessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.SessionRequest,
      edu.eci.arsw.excercise6_3.gym.SessionResponse> getReserveSessionMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.SessionRequest, edu.eci.arsw.excercise6_3.gym.SessionResponse> getReserveSessionMethod;
    if ((getReserveSessionMethod = GymServiceGrpc.getReserveSessionMethod) == null) {
      synchronized (GymServiceGrpc.class) {
        if ((getReserveSessionMethod = GymServiceGrpc.getReserveSessionMethod) == null) {
          GymServiceGrpc.getReserveSessionMethod = getReserveSessionMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise6_3.gym.SessionRequest, edu.eci.arsw.excercise6_3.gym.SessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReserveSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.gym.SessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.gym.SessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GymServiceMethodDescriptorSupplier("ReserveSession"))
              .build();
        }
      }
    }
    return getReserveSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest,
      edu.eci.arsw.excercise6_3.gym.SessionList> getGetSessionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSessions",
      requestType = edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest.class,
      responseType = edu.eci.arsw.excercise6_3.gym.SessionList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest,
      edu.eci.arsw.excercise6_3.gym.SessionList> getGetSessionsMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest, edu.eci.arsw.excercise6_3.gym.SessionList> getGetSessionsMethod;
    if ((getGetSessionsMethod = GymServiceGrpc.getGetSessionsMethod) == null) {
      synchronized (GymServiceGrpc.class) {
        if ((getGetSessionsMethod = GymServiceGrpc.getGetSessionsMethod) == null) {
          GymServiceGrpc.getGetSessionsMethod = getGetSessionsMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest, edu.eci.arsw.excercise6_3.gym.SessionList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSessions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.gym.SessionList.getDefaultInstance()))
              .setSchemaDescriptor(new GymServiceMethodDescriptorSupplier("GetSessions"))
              .build();
        }
      }
    }
    return getGetSessionsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.CancelSessionRequest,
      edu.eci.arsw.excercise6_3.gym.CancelSessionResponse> getCancelSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelSession",
      requestType = edu.eci.arsw.excercise6_3.gym.CancelSessionRequest.class,
      responseType = edu.eci.arsw.excercise6_3.gym.CancelSessionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.CancelSessionRequest,
      edu.eci.arsw.excercise6_3.gym.CancelSessionResponse> getCancelSessionMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.CancelSessionRequest, edu.eci.arsw.excercise6_3.gym.CancelSessionResponse> getCancelSessionMethod;
    if ((getCancelSessionMethod = GymServiceGrpc.getCancelSessionMethod) == null) {
      synchronized (GymServiceGrpc.class) {
        if ((getCancelSessionMethod = GymServiceGrpc.getCancelSessionMethod) == null) {
          GymServiceGrpc.getCancelSessionMethod = getCancelSessionMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise6_3.gym.CancelSessionRequest, edu.eci.arsw.excercise6_3.gym.CancelSessionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.gym.CancelSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.gym.CancelSessionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GymServiceMethodDescriptorSupplier("CancelSession"))
              .build();
        }
      }
    }
    return getCancelSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.GymEmpty,
      edu.eci.arsw.excercise6_3.gym.SessionList> getGetAllSessionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllSessions",
      requestType = edu.eci.arsw.excercise6_3.gym.GymEmpty.class,
      responseType = edu.eci.arsw.excercise6_3.gym.SessionList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.GymEmpty,
      edu.eci.arsw.excercise6_3.gym.SessionList> getGetAllSessionsMethod() {
    io.grpc.MethodDescriptor<edu.eci.arsw.excercise6_3.gym.GymEmpty, edu.eci.arsw.excercise6_3.gym.SessionList> getGetAllSessionsMethod;
    if ((getGetAllSessionsMethod = GymServiceGrpc.getGetAllSessionsMethod) == null) {
      synchronized (GymServiceGrpc.class) {
        if ((getGetAllSessionsMethod = GymServiceGrpc.getGetAllSessionsMethod) == null) {
          GymServiceGrpc.getGetAllSessionsMethod = getGetAllSessionsMethod =
              io.grpc.MethodDescriptor.<edu.eci.arsw.excercise6_3.gym.GymEmpty, edu.eci.arsw.excercise6_3.gym.SessionList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllSessions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.gym.GymEmpty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.eci.arsw.excercise6_3.gym.SessionList.getDefaultInstance()))
              .setSchemaDescriptor(new GymServiceMethodDescriptorSupplier("GetAllSessions"))
              .build();
        }
      }
    }
    return getGetAllSessionsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GymServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GymServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GymServiceStub>() {
        @java.lang.Override
        public GymServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GymServiceStub(channel, callOptions);
        }
      };
    return GymServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GymServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GymServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GymServiceBlockingStub>() {
        @java.lang.Override
        public GymServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GymServiceBlockingStub(channel, callOptions);
        }
      };
    return GymServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GymServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GymServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GymServiceFutureStub>() {
        @java.lang.Override
        public GymServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GymServiceFutureStub(channel, callOptions);
        }
      };
    return GymServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void reserveSession(edu.eci.arsw.excercise6_3.gym.SessionRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReserveSessionMethod(), responseObserver);
    }

    /**
     */
    default void getSessions(edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSessionsMethod(), responseObserver);
    }

    /**
     */
    default void cancelSession(edu.eci.arsw.excercise6_3.gym.CancelSessionRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.CancelSessionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelSessionMethod(), responseObserver);
    }

    /**
     */
    default void getAllSessions(edu.eci.arsw.excercise6_3.gym.GymEmpty request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllSessionsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GymService.
   */
  public static abstract class GymServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GymServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GymService.
   */
  public static final class GymServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GymServiceStub> {
    private GymServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GymServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GymServiceStub(channel, callOptions);
    }

    /**
     */
    public void reserveSession(edu.eci.arsw.excercise6_3.gym.SessionRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReserveSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSessions(edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSessionsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelSession(edu.eci.arsw.excercise6_3.gym.CancelSessionRequest request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.CancelSessionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllSessions(edu.eci.arsw.excercise6_3.gym.GymEmpty request,
        io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllSessionsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GymService.
   */
  public static final class GymServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GymServiceBlockingStub> {
    private GymServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GymServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GymServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.eci.arsw.excercise6_3.gym.SessionResponse reserveSession(edu.eci.arsw.excercise6_3.gym.SessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReserveSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise6_3.gym.SessionList getSessions(edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSessionsMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise6_3.gym.CancelSessionResponse cancelSession(edu.eci.arsw.excercise6_3.gym.CancelSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelSessionMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.eci.arsw.excercise6_3.gym.SessionList getAllSessions(edu.eci.arsw.excercise6_3.gym.GymEmpty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllSessionsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GymService.
   */
  public static final class GymServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GymServiceFutureStub> {
    private GymServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GymServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GymServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise6_3.gym.SessionResponse> reserveSession(
        edu.eci.arsw.excercise6_3.gym.SessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReserveSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise6_3.gym.SessionList> getSessions(
        edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSessionsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise6_3.gym.CancelSessionResponse> cancelSession(
        edu.eci.arsw.excercise6_3.gym.CancelSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelSessionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.eci.arsw.excercise6_3.gym.SessionList> getAllSessions(
        edu.eci.arsw.excercise6_3.gym.GymEmpty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllSessionsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RESERVE_SESSION = 0;
  private static final int METHODID_GET_SESSIONS = 1;
  private static final int METHODID_CANCEL_SESSION = 2;
  private static final int METHODID_GET_ALL_SESSIONS = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RESERVE_SESSION:
          serviceImpl.reserveSession((edu.eci.arsw.excercise6_3.gym.SessionRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionResponse>) responseObserver);
          break;
        case METHODID_GET_SESSIONS:
          serviceImpl.getSessions((edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionList>) responseObserver);
          break;
        case METHODID_CANCEL_SESSION:
          serviceImpl.cancelSession((edu.eci.arsw.excercise6_3.gym.CancelSessionRequest) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.CancelSessionResponse>) responseObserver);
          break;
        case METHODID_GET_ALL_SESSIONS:
          serviceImpl.getAllSessions((edu.eci.arsw.excercise6_3.gym.GymEmpty) request,
              (io.grpc.stub.StreamObserver<edu.eci.arsw.excercise6_3.gym.SessionList>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getReserveSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise6_3.gym.SessionRequest,
              edu.eci.arsw.excercise6_3.gym.SessionResponse>(
                service, METHODID_RESERVE_SESSION)))
        .addMethod(
          getGetSessionsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise6_3.gym.StudentSessionsRequest,
              edu.eci.arsw.excercise6_3.gym.SessionList>(
                service, METHODID_GET_SESSIONS)))
        .addMethod(
          getCancelSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise6_3.gym.CancelSessionRequest,
              edu.eci.arsw.excercise6_3.gym.CancelSessionResponse>(
                service, METHODID_CANCEL_SESSION)))
        .addMethod(
          getGetAllSessionsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              edu.eci.arsw.excercise6_3.gym.GymEmpty,
              edu.eci.arsw.excercise6_3.gym.SessionList>(
                service, METHODID_GET_ALL_SESSIONS)))
        .build();
  }

  private static abstract class GymServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GymServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.eci.arsw.excercise6_3.gym.GymProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GymService");
    }
  }

  private static final class GymServiceFileDescriptorSupplier
      extends GymServiceBaseDescriptorSupplier {
    GymServiceFileDescriptorSupplier() {}
  }

  private static final class GymServiceMethodDescriptorSupplier
      extends GymServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GymServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GymServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GymServiceFileDescriptorSupplier())
              .addMethod(getReserveSessionMethod())
              .addMethod(getGetSessionsMethod())
              .addMethod(getCancelSessionMethod())
              .addMethod(getGetAllSessionsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
